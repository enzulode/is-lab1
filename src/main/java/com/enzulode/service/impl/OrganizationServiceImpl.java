package com.enzulode.service.impl;

import com.enzulode.dao.entity.Address;
import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dao.entity.Organization;
import com.enzulode.dao.repository.AddressRepository;
import com.enzulode.dao.repository.CoordinatesRepository;
import com.enzulode.dao.repository.OrganizationRepository;
import com.enzulode.dto.*;
import com.enzulode.exception.*;
import com.enzulode.service.OrganizationService;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final CoordinatesRepository coordinatesRepository;
  private final AddressRepository addressRepository;
  private final OrganizationRepository organizationRepository;

  public OrganizationServiceImpl(
      CoordinatesRepository coordinatesRepository,
      AddressRepository addressRepository,
      OrganizationRepository organizationRepository) {
    this.coordinatesRepository = coordinatesRepository;
    this.addressRepository = addressRepository;
    this.organizationRepository = organizationRepository;
  }

  @Override
  @Transactional
  public OrganizationReadDto create(OrganizationMutationDto data) {
    // formatter:off
    Coordinates existingCoordinates = coordinatesRepository.findByIdAndCreatedBy(data.coordinatesId(), findUserName())
        .orElseThrow(() -> new CoordinatesNotFoundException("Failed to create organization: coordinates not found"));
    Address existingOfficialAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to create organization: official address not found"));
    Address existingPostalAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to create organization: postal address not found"));

    Organization newOrganization =
        new Organization(
            data.name(),
            existingCoordinates,
            data.creationDate(),
            existingOfficialAddress,
            data.annualTurnover(),
            data.employeesCount(),
            data.rating(),
            data.fullName(),
            data.type(),
            existingPostalAddress);
    Organization result = organizationRepository.save(newOrganization);
    return OrganizationReadDto.toReadDtoForRead(result);
    // formatter:on
  }

  @Override
  public Page<OrganizationReadDto> findAll(Pageable pageable) {
    return organizationRepository.findAll(pageable).map(OrganizationReadDto::toReadDtoForRead);
  }

  @Override
  @Transactional
  public OrganizationReadDto update(Integer id, OrganizationMutationDto data) {
    // formatter:off
    Coordinates existingCoordinates = coordinatesRepository.findByIdAndCreatedBy(data.coordinatesId(), findUserName())
        .orElseThrow(() -> new CoordinatesNotFoundException("Failed to update organization: coordinates not found"));
    Address existingOfficialAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to update organization: official address not found"));
    Address existingPostalAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to update organization: postal address not found"));
    Organization existingOrganization = organizationRepository.findByIdAndCreatedBy(id, findUserName())
        .map(organization -> {
          organization.setName(data.name());
          organization.setCoordinates(existingCoordinates);
          organization.setCreationDate(data.creationDate());
          organization.setOfficialAddress(existingOfficialAddress);
          organization.setAnnualTurnover(data.annualTurnover());
          organization.setEmployeesCount(data.employeesCount());
          organization.setRating(data.rating());
          organization.setFullName(data.fullName());
          organization.setType(data.type());
          organization.setPostalAddress(existingPostalAddress);
          return organization;
        })
        .orElseThrow(() -> new OrganizationNotFoundException("Failed to update organization: organization not found"));
    Organization result = organizationRepository.save(existingOrganization);
    return OrganizationReadDto.toReadDtoForRead(result);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    organizationRepository.deleteByIdAndCreatedBy(id, findUserName());
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
