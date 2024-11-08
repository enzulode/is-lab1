package com.enzulode.service.impl;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.AddressMutationDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.dto.LocationReadDto;
import com.enzulode.exception.AddressCreationFailedException;
import com.enzulode.exception.AddressNotFoundException;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.exception.UnauthorizedOperationException;
import com.enzulode.service.AddressService;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

  private final LocationRepository locationRepository;
  private final AddressRepository addressRepository;

  public AddressServiceImpl(
      LocationRepository locationRepository, AddressRepository addressRepository) {
    this.locationRepository = locationRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  @Transactional
  public AddressReadDto create(AddressMutationDto data) {
    // formatter:off
    Location existingTown = locationRepository.findByIdAndCreatedBy(data.townId(), findUserName())
        .orElseThrow(() -> new LocationNotFoundException("Failed to create address: town not found"));
    Address newAddress = new Address(data.street(), existingTown);
    Address result = addressRepository.save(newAddress);
    if (result.getId() == null) throw new AddressCreationFailedException("Failed to save address to the DB");
    return convertEntityToReadDto(result);
    // formatter:on
  }

  @Override
  public Page<AddressReadDto> findAll(Pageable pageable) {
    return addressRepository.findAll(pageable).map(this::convertEntityToReadDto);
  }

  @Override
  @Transactional
  public AddressReadDto update(Long id, AddressMutationDto data) {
    // formatter:off
    Address existingAddress = addressRepository.findByIdAndCreatedBy(id, findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Unable to update address: the old one not found"));
    Location newTown = locationRepository.findByIdAndCreatedBy(data.townId(), findUserName())
            .orElseThrow(() -> new LocationNotFoundException("Failed to update address: new town was not found"));
    existingAddress.setStreet(data.street());
    existingAddress.setTown(newTown);
    Address result = addressRepository.save(existingAddress);
    return convertEntityToReadDto(result);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    addressRepository.deleteByIdAndCreatedBy(id, findUserName());
  }

  private AddressReadDto convertEntityToReadDto(Address entity) {
    LocationReadDto locationReadDto =
        new LocationReadDto(
            entity.getTown().getId(),
            entity.getTown().getX(),
            entity.getTown().getY(),
            entity.getTown().getZ());
    return new AddressReadDto(entity.getId(), entity.getStreet(), locationReadDto);
  }

  private String findUserName() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    // formatter:off
    if (principal.getName() == null)
      throw new UnauthorizedOperationException(
          "Unable to perform operation: no information about the authentication is present."
      );
    return principal.getName();
    // formatter:on
  }
}
