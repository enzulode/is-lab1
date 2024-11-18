package com.enzulode.service;

import com.enzulode.dto.LocationCreateDto;
import com.enzulode.dto.LocationReadDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {

  LocationReadDto create(LocationCreateDto createDto);

  Page<LocationReadDto> findAll(Pageable pageable);

  LocationReadDto update(Long id, JsonNode patchNode);

  void delete(Long id);
}
