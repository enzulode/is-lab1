package com.enzulode.service.impl;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.AddressMutationDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.exception.AddressNotFoundException;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.service.AddressService;
import com.enzulode.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

  private final LocationRepository locationRepository;
  private final AddressRepository addressRepository;
  private final SecurityContextHelper contextHelper;

  public AddressServiceImpl(
      LocationRepository locationRepository,
      AddressRepository addressRepository,
      SecurityContextHelper contextHelper) {
    this.locationRepository = locationRepository;
    this.addressRepository = addressRepository;
    this.contextHelper = contextHelper;
  }

  @Override
  @Transactional
  public AddressReadDto create(AddressMutationDto data) {
    // formatter:off
    Location existingTown = locationRepository.findByIdAndCreatedBy(data.townId(), contextHelper.findUserName())
        .orElseThrow(() -> new LocationNotFoundException("Failed to create address: town not found"));
    Address newAddress = new Address(data.street(), existingTown);
    Address result = addressRepository.save(newAddress);
    return AddressReadDto.toReadDtoForRead(result);
    // formatter:on
  }

  @Override
  public Page<AddressReadDto> findAll(Pageable pageable) {
    return addressRepository.findAll(pageable).map(AddressReadDto::toReadDtoForRead);
  }

  @Override
  @Transactional
  public AddressReadDto update(Long id, AddressMutationDto data) {
    // formatter:off
    Location newTown = locationRepository.findByIdAndCreatedBy(data.townId(), contextHelper.findUserName())
            .orElseThrow(() -> new LocationNotFoundException("Failed to update address: new town was not found"));
    Address existingAddressWithUpdatedFields = addressRepository.findByIdAndCreatedBy(id, contextHelper.findUserName())
        .map(address -> {
          address.setStreet(data.street());
          address.setTown(newTown);
          return address;
        })
        .orElseThrow(() -> new AddressNotFoundException("Unable to update address: the old one not found"));
    Address updatedAddress = addressRepository.save(existingAddressWithUpdatedFields);
    return AddressReadDto.toReadDtoForRead(updatedAddress);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    addressRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
