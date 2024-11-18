package com.enzulode.service;

import com.enzulode.dto.OrganizationCreateDto;
import com.enzulode.dto.OrganizationReadDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

  OrganizationReadDto create(OrganizationCreateDto createDto);

  Page<OrganizationReadDto> findAll(Pageable pageable);

  OrganizationReadDto update(Integer id, JsonNode patchNode);

  void delete(Long id);
}
