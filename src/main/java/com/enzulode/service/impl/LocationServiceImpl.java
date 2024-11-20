package com.enzulode.service.impl;

import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.LocationCreateDto;
import com.enzulode.dto.LocationReadDto;
import com.enzulode.dto.mapper.LocationMapper;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.service.LocationService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repository;
  private final SecurityContextHelper contextHelper;
  private final LocationMapper locationMapper;
  private final PatchUtil patchUtil;

  public LocationServiceImpl(
      LocationRepository repository,
      SecurityContextHelper contextHelper,
      LocationMapper locationMapper,
      PatchUtil patchUtil) {
    this.repository = repository;
    this.contextHelper = contextHelper;
    this.locationMapper = locationMapper;
    this.patchUtil = patchUtil;
  }

  @Override
  @Transactional
  public LocationReadDto create(LocationCreateDto createDto) {
    // formatter:off
    Location newLocation = locationMapper.toEntity(createDto);
    Location result = repository.save(newLocation);
    return locationMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<LocationReadDto> findAll(Pageable pageable) {
    if (contextHelper.isAdmin()) {
      return repository.findAll(pageable).map(locationMapper::toReadDto);
    }
    return repository
        .findByCreatedBy(contextHelper.findUserName(), pageable)
        .map(locationMapper::toReadDto);
  }

  @Override
  @Transactional
  public LocationReadDto update(Long id, JsonNode patchNode) {
    // formatter:off
    Location location;
    if (contextHelper.isAdmin()) {
      location =
          repository
              .findById(id)
              .orElseThrow(LocationNotFoundException::new);
    } else {
      location =
          repository
              .findByIdAndCreatedBy(id, contextHelper.findUserName())
              .orElseThrow(LocationNotFoundException::new);
    }

    Location patchedLocation = patchUtil.applyPatch(location, patchNode);
    Location patchResult = repository.save(patchedLocation);

    return locationMapper.toReadDto(patchResult);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (contextHelper.isAdmin()) {
      repository.deleteById(id);
    }
    repository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
