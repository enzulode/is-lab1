package com.enzulode.service.impl;

import static com.enzulode.dto.EntityUpdateNotificationDto.NotificationType.*;

import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dao.repository.CoordinatesRepository;
import com.enzulode.dto.CoordinatesCreateDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.dto.EntityUpdateNotificationDto;
import com.enzulode.dto.mapper.CoordinatesMapper;
import com.enzulode.exception.CoordinatesNotFoundException;
import com.enzulode.service.CoordinatesService;
import com.enzulode.service.RabbitMQProducerService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesServiceImpl implements CoordinatesService {

  private final CoordinatesRepository repository;
  private final SecurityContextHelper contextHelper;
  private final CoordinatesMapper coordinatesMapper;
  private final PatchUtil patchUtil;
  private final RabbitMQProducerService producerService;

  public CoordinatesServiceImpl(
      CoordinatesRepository repository,
      SecurityContextHelper contextHelper,
      CoordinatesMapper coordinatesMapper,
      PatchUtil patchUtil,
      RabbitMQProducerService producerService) {
    this.repository = repository;
    this.contextHelper = contextHelper;
    this.coordinatesMapper = coordinatesMapper;
    this.patchUtil = patchUtil;
    this.producerService = producerService;
  }

  @Override
  @Transactional
  public CoordinatesReadDto create(CoordinatesCreateDto data) {
    // formatter:off
    Coordinates newCoordinates = coordinatesMapper.toEntity(data);
    Coordinates result = repository.save(newCoordinates);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_CREATION);
    producerService.sendToRabbitMQ(updateDto);

    return coordinatesMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<CoordinatesReadDto> findAll(Pageable pageable) {
    if (contextHelper.isAdmin()) {
      return repository.findAll(pageable).map(coordinatesMapper::toReadDto);
    }

    return repository
        .findByCreatedBy(contextHelper.findUserName(), pageable)
        .map(coordinatesMapper::toReadDto);
  }

  @Override
  @Transactional
  public CoordinatesReadDto update(Long id, JsonNode patchNode) {
    // formatter:off
    Coordinates coordinates;
    if (contextHelper.isAdmin()) {
      coordinates =
          repository
              .findById(id)
              .orElseThrow(CoordinatesNotFoundException::new);
    } else {
      coordinates =
          repository
              .findByIdAndCreatedBy(id, contextHelper.findUserName())
              .orElseThrow(CoordinatesNotFoundException::new);
    }

    Coordinates patchedCoordinates = patchUtil.applyPatch(coordinates, patchNode);
    Coordinates patchResult = repository.save(patchedCoordinates);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto);

    return coordinatesMapper.toReadDto(patchResult);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (contextHelper.isAdmin()) {
      repository.deleteById(id);
    }

    repository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_DELETION);
    producerService.sendToRabbitMQ(updateDto);
  }
}
