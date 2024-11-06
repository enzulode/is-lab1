package com.enzulode.service.impl;

import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.LocationMutationDto;
import com.enzulode.dto.LocationReadDto;
import com.enzulode.exception.LocationCreationFailedException;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.exception.UnauthorizedOperationException;
import com.enzulode.service.LocationService;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repository;

  public LocationServiceImpl(LocationRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public LocationReadDto create(LocationMutationDto data) {
    // formatter:off
    Location newLocation = new Location(data.x(), data.y(), data.z());
    Location result = repository.save(newLocation);
    if (result.getId() == null) throw new LocationCreationFailedException("Failed to save location to the DB");
    return new LocationReadDto(result.getId(), result.getX(), result.getY(), result.getZ());
    // formatter:on
  }

  @Override
  public Page<LocationReadDto> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::convertEntityToReadDto);
  }

  @Override
  @Transactional
  public LocationReadDto update(Long id, LocationMutationDto data) {
    // formatter:off
    Location existingLocation = repository.findByIdAndCreatedBy(id, findUserName())
        .orElseThrow(() -> new LocationNotFoundException("Unable to update location: the old one not found"));
    existingLocation.setX(data.x());
    existingLocation.setY(data.y());
    existingLocation.setZ(data.z());
    Location updatedLocation = repository.save(existingLocation);
    return new LocationReadDto(updatedLocation.getId(),
        updatedLocation.getX(), updatedLocation.getY(), updatedLocation.getZ());
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteByIdAndCreatedBy(id, findUserName());
  }

  private LocationReadDto convertEntityToReadDto(Location entity) {
    return new LocationReadDto(entity.getId(), entity.getX(), entity.getY(), entity.getZ());
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
