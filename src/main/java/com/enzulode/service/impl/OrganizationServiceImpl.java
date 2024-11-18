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
    Coordinates existingCoordinates = coordinatesRepository.findByIdAndCreatedBy(createDto.coordinatesId(), contextHelper.findUserName())
        .orElseThrow(() -> new CoordinatesNotFoundException("Failed to create organization: coordinates not found"));
    Address existingOfficialAddress = addressRepository.findByIdAndCreatedBy(createDto.officialAddressId(), contextHelper.findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to create organization: official address not found"));
    Address existingPostalAddress = addressRepository.findByIdAndCreatedBy(createDto.postalAddressId(), contextHelper.findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to create organization: postal address not found"));

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
    Organization organization = organizationRepository.findByIdAndCreatedBy(id, contextHelper.findUserName())
        .orElseThrow(() -> new OrganizationNotFoundException("Failed to update organization: organization not found"));

    Organization patchedOrganization = patchUtil.applyPatch(organization, patchNode);
    Organization patchedResult = organizationRepository.save(patchedOrganization);

    return organizationMapper.toReadDto(patchedResult);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    organizationRepository.deleteByIdAndCreatedBy(id, contextHelper.findUserName());
  }
}
