package com.enzulode.service.impl;

import static com.enzulode.dto.EntityUpdateNotificationDto.NotificationType.*;

import com.enzulode.dao.entity.Employee;
import com.enzulode.dao.repository.EmployeeRepository;
import com.enzulode.dto.EmployeeCreateDto;
import com.enzulode.dto.EmployeeReadDto;
import com.enzulode.dto.EntityUpdateNotificationDto;
import com.enzulode.dto.mapper.EmployeeMapper;
import com.enzulode.exception.EmployeeNotFoundException;
import com.enzulode.service.EmployeeService;
import com.enzulode.service.RabbitMQProducerService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final SecurityContextHelper contextHelper;
  private final EmployeeMapper employeeMapper;
  private final PatchUtil patchUtil;
  private final RabbitMQProducerService producerService;

  private final String routingKey = "updates.employee";

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository,
      SecurityContextHelper contextHelper,
      EmployeeMapper employeeMapper,
      PatchUtil patchUtil,
      RabbitMQProducerService producerService) {
    this.employeeRepository = employeeRepository;
    this.contextHelper = contextHelper;
    this.employeeMapper = employeeMapper;
    this.patchUtil = patchUtil;
    this.producerService = producerService;
  }

  @Override
  @Transactional
  public EmployeeReadDto create(EmployeeCreateDto createDto) {
    // formatter:off
    Employee employee = employeeMapper.toEntity(createDto);
    Employee result = employeeRepository.save(employee);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_CREATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return employeeMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<EmployeeReadDto> findAll(Pageable pageable) {
    return employeeRepository.findAll(pageable).map(employeeMapper::toReadDto);
  }

  @Override
  @Transactional
  public EmployeeReadDto update(Long id, JsonNode patchNode) {
    // formatter:off
    Employee employee = employeeRepository
        .findByIdAndCreatedBy(id, contextHelper.findUserName())
        .orElseThrow(EmployeeNotFoundException::new);

    Employee patchedEmployee = patchUtil.applyPatchPreserve(employee, patchNode, List.of("organizations"));
    Employee patchResult = employeeRepository.save(patchedEmployee);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return employeeMapper.toReadDto(patchResult);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    employeeRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_DELETION);
    producerService.sendToRabbitMQ(updateDto, routingKey);
  }
}
