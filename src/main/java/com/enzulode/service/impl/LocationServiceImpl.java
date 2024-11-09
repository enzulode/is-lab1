package com.enzulode.service.impl;

import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.LocationMutationDto;
import com.enzulode.dto.LocationReadDto;
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
    Location newLocation = LocationMutationDto.toEntityForCreate(data);
    Location result = repository.save(newLocation);
    return LocationReadDto.toReadDtoForRead(result);
    // formatter:on
  }

  @Override
  public Page<LocationReadDto> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(LocationReadDto::toReadDtoForRead);
  }

  @Override
  @Transactional
  public LocationReadDto update(Long id, LocationMutationDto data) {
    // formatter:off
    Location existingLocationWithUpdatedFields = repository.findByIdAndCreatedBy(id, findUserName())
        .map(location -> LocationMutationDto.toEntityForUpdate(location, data))
        .orElseThrow(() -> new LocationNotFoundException("Unable to update location: the old one not found"));
    Location updatedLocation = repository.save(existingLocationWithUpdatedFields);
    return LocationReadDto.toReadDtoForRead(updatedLocation);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteByIdAndCreatedBy(id, findUserName());  
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
