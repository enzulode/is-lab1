package com.enzulode.service;

import com.enzulode.dto.CoordinatesMutationDto;
import com.enzulode.dto.CoordinatesReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoordinatesService {

  CoordinatesReadDto create(CoordinatesMutationDto data);

  Page<CoordinatesReadDto> findAll(Pageable pageable);

  CoordinatesReadDto update(Long id, CoordinatesMutationDto data);

  void delete(Long id);
}
