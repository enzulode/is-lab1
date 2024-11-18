package com.enzulode.service.impl;

import com.enzulode.dao.entity.Employee;
import com.enzulode.dao.repository.EmployeeRepository;
import com.enzulode.dto.EmployeeCreateDto;
import com.enzulode.dto.EmployeeReadDto;
import com.enzulode.dto.mapper.EmployeeMapper;
import com.enzulode.exception.EmployeeNotFoundException;
import com.enzulode.service.EmployeeService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
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

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository,
      SecurityContextHelper contextHelper,
      EmployeeMapper employeeMapper,
      PatchUtil patchUtil) {
    this.employeeRepository = employeeRepository;
    this.contextHelper = contextHelper;
    this.employeeMapper = employeeMapper;
    this.patchUtil = patchUtil;
  }

  @Override
  @Transactional
  public EmployeeReadDto create(EmployeeCreateDto createDto) {
    // formatter:off
    Employee employee = employeeMapper.toEntity(createDto);
    Employee result = employeeRepository.save(employee);
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

    Employee patchedEmployee = patchUtil.applyPatch(employee, patchNode);
    Employee patchResult = employeeRepository.save(patchedEmployee);

    return employeeMapper.toReadDto(patchResult);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    employeeRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
