package com.enzulode.service;

import com.enzulode.dto.CoordinatesCreateDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoordinatesService {

  CoordinatesReadDto create(CoordinatesCreateDto createDto);

  Page<CoordinatesReadDto> findAll(Pageable pageable);

  CoordinatesReadDto update(Long id, JsonNode patchNode);

  void delete(Long id);
}
