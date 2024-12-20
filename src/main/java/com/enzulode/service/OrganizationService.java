package com.enzulode.service;

import com.enzulode.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

  OrganizationReadDto create(OrganizationCreateDto createDto);

  Page<OrganizationReadDto> findAll(Pageable pageable);

  OrganizationReadDto update(Integer id, JsonNode patchNode);

  OrganizationReadDto updateCoordinates(Integer organizationId, Long coordinatesId);

  OrganizationReadDto updateOfficialAddress(Integer organizationId, Long officialAddressId);

  OrganizationReadDto updatePostalAddress(Integer organizationId, Long postalAddressId);

  void delete(Integer id);

  OrganizationsTotalRatingDto totalRating();

  CountOrganizationFullNameLessThanDto countOrganizationFullNameLessThan(String fullName);

  CountOrganizationFullNameMoreThanDto countOrganizationFullNameMoreThan(String fullName);

  void removeAllEmployeesOnOrganization(Integer organizationId);

  void hireEmployeeToOrganization(Integer organizationId, Long employeeId);
}
