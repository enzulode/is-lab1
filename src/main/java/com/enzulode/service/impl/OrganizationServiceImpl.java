package com.enzulode.service.impl;

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

  public OrganizationServiceImpl(
      CoordinatesRepository coordinatesRepository,
      AddressRepository addressRepository,
      OrganizationRepository organizationRepository,
      SecurityContextHelper contextHelper,
      OrganizationMapper organizationMapper,
      PatchUtil patchUtil) {
    this.coordinatesRepository = coordinatesRepository;
    this.addressRepository = addressRepository;
    this.organizationRepository = organizationRepository;
    this.contextHelper = contextHelper;
    this.organizationMapper = organizationMapper;
    this.patchUtil = patchUtil;
  }

  @Override
  @Transactional
  public OrganizationReadDto create(OrganizationCreateDto createDto) {
    // formatter:off
    Coordinates existingCoordinates = coordinatesRepository
        .findByIdAndCreatedBy(createDto.coordinatesId(), contextHelper.findUserName())
        .orElseThrow(CoordinatesNotFoundException::new);

    Address existingOfficialAddress = addressRepository
        .findByIdAndCreatedBy(createDto.officialAddressId(), contextHelper.findUserName())
        .orElseThrow(AddressNotFoundException::new);

    Address existingPostalAddress = addressRepository
        .findByIdAndCreatedBy(createDto.postalAddressId(), contextHelper.findUserName())
        .orElseThrow(AddressNotFoundException::new);

    Organization organization = organizationMapper.toEntity(createDto);
    organization.setCoordinates(existingCoordinates);
    organization.setOfficialAddress(existingOfficialAddress);
    organization.setPostalAddress(existingPostalAddress);

    Organization result = organizationRepository.save(organization);
    return organizationMapper.toReadDto(result);
    // formatter:on
  }

  @Override
  public Page<OrganizationReadDto> findAll(Pageable pageable) {
    return organizationRepository.findAll(pageable).map(organizationMapper::toReadDto);
  }

  @Override
  @Transactional
  public OrganizationReadDto update(Integer id, JsonNode patchNode) {
    // formatter:off
    Organization organization = organizationRepository
        .findByIdAndCreatedBy(id, contextHelper.findUserName())
        .orElseThrow(OrganizationNotFoundException::new);

    List<String> preserve = List.of("coordinates", "officialAddress", "postalAddress");
    Organization patchedOrganization = patchUtil.applyPatchPreserve(organization, patchNode, preserve);
    Organization patchedResult = organizationRepository.save(patchedOrganization);

    return organizationMapper.toReadDto(patchedResult);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updateCoordinates(Integer organizationId, Long coordinatesId) {
    // formatter:off
    Organization organization = organizationRepository
        .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
        .orElseThrow(OrganizationNotFoundException::new);

    Coordinates coordinates = coordinatesRepository
        .findByIdAndCreatedBy(coordinatesId, contextHelper.findUserName())
        .orElseThrow(CoordinatesNotFoundException::new);

    organization.setCoordinates(coordinates);
    Organization updatedOrganization = organizationRepository.save(organization);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updateOfficialAddress(Integer organizationId, Long officialAddressId) {
    // formatter:off
    Organization organization = organizationRepository
        .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
        .orElseThrow(OrganizationNotFoundException::new);

    Address address = addressRepository
        .findByIdAndCreatedBy(officialAddressId, contextHelper.findUserName())
        .orElseThrow(AddressNotFoundException::new);

    organization.setOfficialAddress(address);
    Organization updatedOrganization = organizationRepository.save(organization);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public OrganizationReadDto updatePostalAddress(Integer organizationId, Long postalAddressId) {
    // formatter:off
    Organization organization = organizationRepository
        .findByIdAndCreatedBy(organizationId, contextHelper.findUserName())
        .orElseThrow(OrganizationNotFoundException::new);

    Address address = addressRepository
        .findByIdAndCreatedBy(postalAddressId, contextHelper.findUserName())
        .orElseThrow(AddressNotFoundException::new);

    organization.setPostalAddress(address);
    Organization updatedOrganization = organizationRepository.save(organization);

    return organizationMapper.toReadDto(updatedOrganization);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    organizationRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
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
