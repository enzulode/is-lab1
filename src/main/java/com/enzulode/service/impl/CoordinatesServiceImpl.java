package com.enzulode.service.impl;

import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dao.repository.CoordinatesRepository;
import com.enzulode.dto.CoordinatesMutationDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.exception.CoordinatesNotFoundException;
import com.enzulode.service.CoordinatesService;
import com.enzulode.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesServiceImpl implements CoordinatesService {

  private final CoordinatesRepository repository;
  private final SecurityContextHelper contextHelper;

  public CoordinatesServiceImpl(
      CoordinatesRepository repository, SecurityContextHelper contextHelper) {
    this.repository = repository;
    this.contextHelper = contextHelper;
  }

  @Override
  @Transactional
  public CoordinatesReadDto create(CoordinatesMutationDto data) {
    // formatter:off
    Coordinates newCoordinates = CoordinatesMutationDto.toEntityForCreate(data);
    Coordinates result = repository.save(newCoordinates);
    return CoordinatesReadDto.toReadDtoForRead(result);
    // formatter:on
  }

  @Override
  public Page<CoordinatesReadDto> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(CoordinatesReadDto::toReadDtoForRead);
  }

  @Override
  @Transactional
  public CoordinatesReadDto update(Long id, CoordinatesMutationDto data) {
    // formatter:off
    Coordinates existingCoordinatesWithUpdatedFields = repository.findByIdAndCreatedBy(id, contextHelper.findUserName())
        .map(coords -> CoordinatesMutationDto.toEntityForUpdate(coords, data))
        .orElseThrow(() -> new CoordinatesNotFoundException("Unable to update coordinates: the old one not found"));
    Coordinates updatedCoordinates = repository.save(existingCoordinatesWithUpdatedFields);
    return new CoordinatesReadDto(updatedCoordinates.getId(), updatedCoordinates.getX(), updatedCoordinates.getY());
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
