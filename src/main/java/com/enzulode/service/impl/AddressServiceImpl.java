package com.enzulode.service.impl;

import static com.enzulode.dto.EntityUpdateNotificationDto.NotificationType.*;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Location;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.LocationRepository;
import com.enzulode.dto.AddressCreateDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.dto.EntityUpdateNotificationDto;
import com.enzulode.dto.mapper.AddressMapper;
import com.enzulode.exception.AddressNotFoundException;
import com.enzulode.exception.LocationNotFoundException;
import com.enzulode.service.AddressService;
import com.enzulode.service.RabbitMQProducerService;
import com.enzulode.util.PatchUtil;
import com.enzulode.util.SecurityContextHelper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
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
  private final RabbitMQProducerService producerService;

  public AddressServiceImpl(
      LocationRepository locationRepository,
      AddressRepository addressRepository,
      SecurityContextHelper contextHelper,
      AddressMapper addressMapper,
      PatchUtil patchUtil,
      RabbitMQProducerService producerService) {
    this.locationRepository = locationRepository;
    this.addressRepository = addressRepository;
    this.contextHelper = contextHelper;
    this.addressMapper = addressMapper;
    this.patchUtil = patchUtil;
    this.producerService = producerService;
  }

  @Override
  @Transactional
  public AddressReadDto create(AddressCreateDto createDto) {
    // formatter:off
    Location existingTown;
    if (contextHelper.isAdmin()) {
      existingTown =
          locationRepository
              .findById(createDto.townId())
              .orElseThrow(LocationNotFoundException::new);
    } else {
      existingTown =
          locationRepository
              .findByIdAndCreatedBy(createDto.townId(), contextHelper.findUserName())
              .orElseThrow(LocationNotFoundException::new);
    }

    Address address = addressMapper.toEntity(createDto);
    address.setTown(existingTown);
    Address result = addressRepository.save(address);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_CREATION);
    producerService.sendToRabbitMQ(updateDto);

    return addressMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<AddressReadDto> findAll(Pageable pageable) {
    if (contextHelper.isAdmin()) {
      return addressRepository.findAll(pageable).map(addressMapper::toReadDto);
    }
    return addressRepository
        .findByCreatedBy(contextHelper.findUserName(), pageable)
        .map(addressMapper::toReadDto);
  }

  @Override
  @Transactional
  public AddressReadDto update(Long id, JsonNode patchNode) {
    // formatter:off
    Address address;
    if (contextHelper.isAdmin()) {
      address =
          addressRepository
              .findById(id)
              .orElseThrow(AddressNotFoundException::new);
    } else {
      address =
          addressRepository
              .findByIdAndCreatedBy(id, contextHelper.findUserName())
              .orElseThrow(AddressNotFoundException::new);
    }

    Address patchedAddress = patchUtil.applyPatchPreserve(address, patchNode, List.of("town"));
    Address patchedResult = addressRepository.save(patchedAddress);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto);

    return addressMapper.toReadDto(patchedResult);
    // formatter:on
  }

  @Override
  @Transactional
  public AddressReadDto updateTown(Long addressId, Long townId) {
    // formatter:off
    Address address;
    Location town;
    if (contextHelper.isAdmin()) {
      address =
          addressRepository
              .findById(addressId)
              .orElseThrow(AddressNotFoundException::new);
      town =
          locationRepository
              .findById(townId)
              .orElseThrow(LocationNotFoundException::new);
    } else {
      address =
          addressRepository
              .findByIdAndCreatedBy(addressId, contextHelper.findUserName())
              .orElseThrow(AddressNotFoundException::new);
      town =
          locationRepository
              .findByIdAndCreatedBy(townId, contextHelper.findUserName())
              .orElseThrow(LocationNotFoundException::new);
    }

    address.setTown(town);
    Address updatedAddress = addressRepository.save(address);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto);

    return addressMapper.toReadDto(updatedAddress);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (contextHelper.isAdmin()) {
      addressRepository.deleteById(id);
    }

    addressRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_DELETION);
    producerService.sendToRabbitMQ(updateDto);
  }
}
