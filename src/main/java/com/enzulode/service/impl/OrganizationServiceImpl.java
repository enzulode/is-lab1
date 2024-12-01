package com.enzulode.service.impl;

import static com.enzulode.dto.EntityUpdateNotificationDto.NotificationType.*;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dao.entity.Organization;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.CoordinatesRepository;
import com.enzulode.dao.repository.OrganizationRepository;
import com.enzulode.dto.*;
import com.enzulode.dto.mapper.OrganizationMapper;
import com.enzulode.exception.*;
import com.enzulode.service.OrganizationService;
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
public class OrganizationServiceImpl implements OrganizationService {

  private final CoordinatesRepository coordinatesRepository;
  private final AddressRepository addressRepository;
  private final OrganizationRepository organizationRepository;
  private final SecurityContextHelper contextHelper;
  private final OrganizationMapper organizationMapper;
  private final PatchUtil patchUtil;
  private final RabbitMQProducerService producerService;

  private final String routingKey = "updates.organization";

  public OrganizationServiceImpl(
      CoordinatesRepository coordinatesRepository,
      AddressRepository addressRepository,
      OrganizationRepository organizationRepository,
      SecurityContextHelper contextHelper,
      OrganizationMapper organizationMapper,
      PatchUtil patchUtil,
      RabbitMQProducerService producerService) {
    this.coordinatesRepository = coordinatesRepository;
    this.addressRepository = addressRepository;
    this.organizationRepository = organizationRepository;
    this.contextHelper = contextHelper;
    this.organizationMapper = organizationMapper;
    this.patchUtil = patchUtil;
    this.producerService = producerService;
  }

  @Override
  @Transactional
  public OrganizationReadDto create(OrganizationCreateDto createDto) {
    // formatter:off
    Coordinates existingCoordinates;
    Address existingOfficialAddress;
    Address existingPostalAddress;
    if (contextHelper.isAdmin()) {
      existingCoordinates =
          coordinatesRepository
              .findById(createDto.coordinatesId())
              .orElseThrow(CoordinatesNotFoundException::new);
      existingOfficialAddress =
          addressRepository
              .findById(createDto.officialAddressId())
              .orElseThrow(AddressNotFoundException::new);
      existingPostalAddress =
          addressRepository
              .findById(createDto.postalAddressId())
              .orElseThrow(AddressNotFoundException::new);
    } else {
      existingCoordinates =
          coordinatesRepository
              .findByIdAndCreatedBy(createDto.coordinatesId(), contextHelper.findUserName())
              .orElseThrow(CoordinatesNotFoundException::new);
      existingOfficialAddress =
          addressRepository
              .findByIdAndCreatedBy(createDto.officialAddressId(), contextHelper.findUserName())
              .orElseThrow(AddressNotFoundException::new);
      existingPostalAddress =
          addressRepository
              .findByIdAndCreatedBy(createDto.postalAddressId(), contextHelper.findUserName())
              .orElseThrow(AddressNotFoundException::new);
    }

    Organization organization = organizationMapper.toEntity(createDto);
    organization.setCoordinates(existingCoordinates);
    organization.setOfficialAddress(existingOfficialAddress);
    organization.setPostalAddress(existingPostalAddress);

    Organization result = organizationRepository.save(organization);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_CREATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return organizationMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<OrganizationReadDto> findAll(Pageable pageable) {
    if (contextHelper.isAdmin()) {
      return organizationRepository.findAll(pageable).map(organizationMapper::toReadDto);
    }
    return organizationRepository
        .findByCreatedBy(contextHelper.findUserName(), pageable)
        .map(organizationMapper::toReadDto);
  }

  @Override
  @Transactional
  public OrganizationReadDto update(Integer id, JsonNode patchNode) {
    // formatter:off
    Organization organization;
    if (contextHelper.isAdmin()) {
      organization =
          organizationRepository
              .findById(id)
              .orElseThrow(OrganizationNotFoundException::new);
    } else {
      organization =
          organizationRepository
              .findByIdAndCreatedBy(id, contextHelper.findUserName())
              .orElseThrow(OrganizationNotFoundException::new);
    }

    List<String> preserve = List.of("coordinates", "officialAddress", "postalAddress");
    Organization patchedOrganization = patchUtil.applyPatchPreserve(organization, patchNode, preserve);
    Organization patchedResult = organizationRepository.save(patchedOrganization);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return organizationMapper.toReadDto(patchedResult);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updateCoordinates(Integer organizationId, Long coordinatesId) {
    // formatter:off
    Organization organization;
    Coordinates coordinates;
    if (contextHelper.isAdmin()) {
      organization =
          organizationRepository
              .findById(organizationId)
              .orElseThrow(OrganizationNotFoundException::new);
      coordinates =
          coordinatesRepository
              .findById(coordinatesId)
              .orElseThrow(CoordinatesNotFoundException::new);
    } else {
      organization =
          organizationRepository
              .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
              .orElseThrow(OrganizationNotFoundException::new);
      coordinates =
          coordinatesRepository
              .findByIdAndCreatedBy(coordinatesId, contextHelper.findUserName())
              .orElseThrow(CoordinatesNotFoundException::new);
    }

    organization.setCoordinates(coordinates);
    Organization updatedOrganization = organizationRepository.save(organization);


    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updateOfficialAddress(Integer organizationId, Long officialAddressId) {
    // formatter:off
    Organization organization;
    Address officialAddress;
    if (contextHelper.isAdmin()) {
      organization =
          organizationRepository
              .findById(organizationId)
              .orElseThrow(OrganizationNotFoundException::new);
      officialAddress =
          addressRepository
              .findById(officialAddressId)
              .orElseThrow(CoordinatesNotFoundException::new);
    } else {
      organization =
          organizationRepository
              .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
              .orElseThrow(OrganizationNotFoundException::new);
      officialAddress =
          addressRepository
              .findByIdAndCreatedBy(officialAddressId, contextHelper.findUserName())
              .orElseThrow(CoordinatesNotFoundException::new);
    }

    organization.setOfficialAddress(officialAddress);
    Organization updatedOrganization = organizationRepository.save(organization);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updatePostalAddress(Integer organizationId, Long postalAddressId) {
    // formatter:off
    Organization organization;
    Address postalAddress;
    if (contextHelper.isAdmin()) {
      organization =
          organizationRepository
              .findById(organizationId)
              .orElseThrow(OrganizationNotFoundException::new);
      postalAddress =
          addressRepository
              .findById(postalAddressId)
              .orElseThrow(CoordinatesNotFoundException::new);
    } else {
      organization =
          organizationRepository
              .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
              .orElseThrow(OrganizationNotFoundException::new);
      postalAddress =
          addressRepository
              .findByIdAndCreatedBy(postalAddressId, contextHelper.findUserName())
              .orElseThrow(CoordinatesNotFoundException::new);
    }

    organization.setPostalAddress(postalAddress);
    Organization updatedOrganization = organizationRepository.save(organization);

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_MODIFICATION);
    producerService.sendToRabbitMQ(updateDto, routingKey);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    if (contextHelper.isAdmin()) {
      organizationRepository.deleteById(id);
    }
    organizationRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ENTITY_DELETION);
    producerService.sendToRabbitMQ(updateDto, routingKey);
  }

  @Override
  public OrganizationsTotalRatingDto totalRating() {
    return new OrganizationsTotalRatingDto(organizationRepository.totalRating());
  }

  @Override
  public CountOrganizationFullNameLessThanDto countOrganizationFullNameLessThan(String fullName) {
    return new CountOrganizationFullNameLessThanDto(
        organizationRepository.countOrganizationFullNameLessThan(fullName));
  }

  @Override
  public CountOrganizationFullNameMoreThanDto countOrganizationFullNameMoreThan(String fullName) {
    return new CountOrganizationFullNameMoreThanDto(
        organizationRepository.countOrganizationFullNameMoreThan(fullName));
  }

  @Override
  @Transactional
  public void removeAllEmployeesOnOrganization(Integer organizationId) {
    organizationRepository.removeAllEmployeesOnOrganization(organizationId);
  }

  @Override
  @Transactional
  public void hireEmployeeToOrganization(Integer organizationId, Long employeeId) {
    organizationRepository.hireEmployeeToOrganization(organizationId, employeeId);
  }
}
