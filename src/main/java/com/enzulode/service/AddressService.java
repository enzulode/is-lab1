package com.enzulode.service;

import com.enzulode.dto.AddressMutationDto;
import com.enzulode.dto.AddressReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

  AddressReadDto create(AddressMutationDto data);

  Page<AddressReadDto> findAll(Pageable pageable);

  AddressReadDto update(Long id, AddressMutationDto data);

  void delete(Long id);
}
