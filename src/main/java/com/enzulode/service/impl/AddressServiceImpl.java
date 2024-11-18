package com.enzulode.service.impl;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.AddressCreateDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.dto.mapper.AddressMapper;
import com.enzulode.exception.AddressNotFoundException;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.service.AddressService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

  private final LocationRepository locationRepository;
  private final AddressRepository addressRepository;
  private final SecurityContextHelper contextHelper;
  private final AddressMapper addressMapper;
  private final PatchUtil patchUtil;

  public AddressServiceImpl(
      LocationRepository locationRepository,
      AddressRepository addressRepository,
      SecurityContextHelper contextHelper,
      AddressMapper addressMapper,
      PatchUtil patchUtil) {
    this.locationRepository = locationRepository;
    this.addressRepository = addressRepository;
    this.contextHelper = contextHelper;
    this.addressMapper = addressMapper;
    this.patchUtil = patchUtil;
  }

  @Override
  @Transactional
  public AddressReadDto create(AddressCreateDto createDto) {
    // formatter:off
    Location existingTown = locationRepository.findByIdAndCreatedBy(createDto.townId(), contextHelper.findUserName())
        .orElseThrow(() -> new LocationNotFoundException("Failed to create address: town not found"));

    Address address = addressMapper.toEntity(createDto);
    address.setTown(existingTown);
    Address result = addressRepository.save(address);
    return addressMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<AddressReadDto> findAll(Pageable pageable) {
    return addressRepository.findAll(pageable).map(addressMapper::toReadDto);
  }

  @Override
  @Transactional
  public AddressReadDto update(Long id, JsonNode patchNode) {
    // formatter:off
    Address address = addressRepository.findByIdAndCreatedBy(id, contextHelper.findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Unable to update address: the old one not found"));

    Address patchedAddress = patchUtil.applyPatch(address, patchNode);
    Address patchedResult = addressRepository.save(patchedAddress);
    return addressMapper.toReadDto(patchedResult);
    // formatter:on
  }

  @Override
  @Transactional
  public AddressReadDto updateTown(Long addressId, Long townId) {
    // formatter:off
    Address address = addressRepository.findByIdAndCreatedBy(addressId, contextHelper.findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Unable to update address: address not found"));
    Location town = locationRepository.findByIdAndCreatedBy(townId, contextHelper.findUserName())
        .orElseThrow(() -> new LocationNotFoundException("Unable to update address town: new town not found"));

    address.setTown(town);
    Address updatedAddress = addressRepository.save(address);

    return addressMapper.toReadDto(updatedAddress);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    addressRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
