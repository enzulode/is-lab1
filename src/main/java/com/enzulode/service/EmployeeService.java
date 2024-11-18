package com.enzulode.service;

import com.enzulode.dto.EmployeeCreateDto;
import com.enzulode.dto.EmployeeReadDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

  EmployeeReadDto create(EmployeeCreateDto createDto);

  Page<EmployeeReadDto> findAll(Pageable pageable);

  EmployeeReadDto update(Long id, JsonNode patchNode);

  void delete(Long id);
}
