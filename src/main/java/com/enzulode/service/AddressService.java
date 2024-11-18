package com.enzulode.service;

import com.enzulode.dto.AddressCreateDto;
import com.enzulode.dto.AddressReadDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

  AddressReadDto create(AddressCreateDto createDto);

  Page<AddressReadDto> findAll(Pageable pageable);

  AddressReadDto update(Long id, JsonNode patchNode);

  void delete(Long id);
}
