package com.enzulode.service.impl;

import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dao.repository.CoordinatesRepository;
import com.enzulode.dto.CoordinatesMutationDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.exception.CoordinatesCreationFailedException;
import com.enzulode.exception.CoordinatesNotFoundException;
import com.enzulode.exception.UnauthorizedOperationException;
import com.enzulode.service.CoordinatesService;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesServiceImpl implements CoordinatesService {

  private final CoordinatesRepository repository;

  public CoordinatesServiceImpl(CoordinatesRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public CoordinatesReadDto create(CoordinatesMutationDto data) {
    // formatter:off
    Coordinates newCoordinates = new Coordinates(data.x(), data.y());
    Coordinates result = repository.save(newCoordinates);
    if (result.getId() == null) throw new CoordinatesCreationFailedException("Failed to save coordinates to the DB");
    return new CoordinatesReadDto(result.getId(), result.getX(), result.getY());
    // formatter:on
  }

  @Override
  public Page<CoordinatesReadDto> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::convertEntityToReadDto);
  }

  @Override
  @Transactional
  public CoordinatesReadDto update(Long id, CoordinatesMutationDto data) {
    // formatter:off
    Coordinates existingCoordinates = repository.findByIdAndCreatedBy(id, findUserName())
        .orElseThrow(() -> new CoordinatesNotFoundException("Unable to update coordinates: the old one not found"));
    existingCoordinates.setX(data.x());
    existingCoordinates.setY(data.y());
    Coordinates updatedCoordinates = repository.save(existingCoordinates);
    return new CoordinatesReadDto(updatedCoordinates.getId(), updatedCoordinates.getX(), updatedCoordinates.getY());
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteByIdAndCreatedBy(id, findUserName());
  }

  private CoordinatesReadDto convertEntityToReadDto(Coordinates entity) {
    return new CoordinatesReadDto(entity.getId(), entity.getX(), entity.getY());
  }

  private String findUserName() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    // formatter:off
    if (principal.getName() == null)
      throw new UnauthorizedOperationException(
          "Unable to perform operation: no information about the authentication is present."
      );
    return principal.getName();
    // formatter:on
  }
}
