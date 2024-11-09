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

    Organization newOrganization = new Organization(data.name(), existingCoordinates, data.creationDate(), existingOfficialAddress, data.annualTurnover(), data.employeesCount(), data.rating(), data.fullName(), data.type(), existingPostalAddress);
    Organization result = organizationRepository.save(newOrganization);
    if (result.getId() == null) throw new OrganizationCreationFailedException("Failed to save organization to the DB");
    // formatter:on
    return convertEntityToReadDto(result);
  }

  @Override
  public Page<OrganizationReadDto> findAll(Pageable pageable) {
    return organizationRepository.findAll(pageable).map(this::convertEntityToReadDto);
  }

  @Override
  @Transactional
  public OrganizationReadDto update(Integer id, OrganizationMutationDto data) {
    // formatter:off
    Organization existingOrganization = organizationRepository.findByIdAndCreatedBy(id, findUserName())
        .orElseThrow(() -> new OrganizationNotFoundException("Failed to update organization: organization not found"));
    Coordinates existingCoordinates = coordinatesRepository.findByIdAndCreatedBy(data.coordinatesId(), findUserName())
        .orElseThrow(() -> new CoordinatesNotFoundException("Failed to update organization: coordinates not found"));
    Address existingOfficialAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to update organization: official address not found"));
    Address existingPostalAddress = addressRepository.findByIdAndCreatedBy(data.officialAddressId(), findUserName())
        .orElseThrow(() -> new AddressNotFoundException("Failed to update organization: postal address not found"));

    existingOrganization.setName(data.name());
    existingOrganization.setCoordinates(existingCoordinates);
    existingOrganization.setCreationDate(data.creationDate());
    existingOrganization.setOfficialAddress(existingOfficialAddress);
    existingOrganization.setAnnualTurnover(data.annualTurnover());
    existingOrganization.setEmployeesCount(data.employeesCount());
    existingOrganization.setRating(data.rating());
    existingOrganization.setFullName(data.fullName());
    existingOrganization.setType(data.type());
    existingOrganization.setPostalAddress(existingPostalAddress);

    Organization result = organizationRepository.save(existingOrganization);
    return convertEntityToReadDto(result);
    // formatter:on
  }

  @Override
  @Transactional
  public void delete(Long id) {
    organizationRepository.deleteByIdAndCreatedBy(id, findUserName());
  }

  private OrganizationReadDto convertEntityToReadDto(Organization organization) {
    CoordinatesReadDto coordinatesReadDto =
        new CoordinatesReadDto(
            organization.getCoordinates().getId(),
            organization.getCoordinates().getX(),
            organization.getCoordinates().getY());

    LocationReadDto officialAddressTownLocation =
        new LocationReadDto(
            organization.getOfficialAddress().getTown().getId(),
            organization.getOfficialAddress().getTown().getX(),
            organization.getOfficialAddress().getTown().getY(),
            organization.getOfficialAddress().getTown().getZ());
    AddressReadDto officialAddressReadDto =
        new AddressReadDto(
            organization.getOfficialAddress().getId(),
            organization.getOfficialAddress().getStreet(),
            officialAddressTownLocation);

    LocationReadDto postalAddressTownLocation =
        new LocationReadDto(
            organization.getPostalAddress().getTown().getId(),
            organization.getPostalAddress().getTown().getX(),
            organization.getPostalAddress().getTown().getY(),
            organization.getPostalAddress().getTown().getZ());
    AddressReadDto postalAddressReadDto =
        new AddressReadDto(
            organization.getOfficialAddress().getId(),
            organization.getOfficialAddress().getStreet(),
            postalAddressTownLocation);

    return new OrganizationReadDto(
        organization.getId(),
        organization.getName(),
        coordinatesReadDto,
        organization.getCreationDate(),
        officialAddressReadDto,
        organization.getAnnualTurnover(),
        organization.getEmployeesCount(),
        organization.getRating(),
        organization.getName(),
        organization.getType(),
        postalAddressReadDto);
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
