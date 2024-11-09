package com.enzulode.service.impl;

import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.LocationMutationDto;
import com.enzulode.dto.LocationReadDto;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.service.LocationService;
import com.enzulode.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repository;
  private final SecurityContextHelper contextHelper;

  public LocationServiceImpl(LocationRepository repository, SecurityContextHelper contextHelper) {
    this.repository = repository;
    this.contextHelper = contextHelper;
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
    Location existingLocationWithUpdatedFields = repository.findByIdAndCreatedBy(id, contextHelper.findUserName())
        .map(location -> LocationMutationDto.toEntityForUpdate(location, data))
        .orElseThrow(() -> new LocationNotFoundException("Unable to update location: the old one not found"));
    Location updatedLocation = repository.save(existingLocationWithUpdatedFields);
    return LocationReadDto.toReadDtoForRead(updatedLocation);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
