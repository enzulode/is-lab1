package com.enzulode.service;

import com.enzulode.dto.LocationMutationDto;
import com.enzulode.dto.LocationReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {

  LocationReadDto create(LocationMutationDto data);

  Page<LocationReadDto> findAll(Pageable pageable);

  LocationReadDto update(Long id, LocationMutationDto data);

  void delete(Long id);
}
