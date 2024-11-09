package com.enzulode.service;

import com.enzulode.dto.OrganizationMutationDto;
import com.enzulode.dto.OrganizationReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

  OrganizationReadDto create(OrganizationMutationDto data);

  Page<OrganizationReadDto> findAll(Pageable pageable);

  OrganizationReadDto update(Integer id, OrganizationMutationDto data);

  void delete(Long id);
}
