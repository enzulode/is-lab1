package com.enzulode.dto;

import com.enzulode.dao.entity.Organization;
import com.enzulode.dao.entity.OrganizationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record OrganizationReadDto(
    // formatter:off
    Integer id,
    String name,
    CoordinatesReadDto coordinates,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime creationDate,
    AddressReadDto officialAddress,
    int annualTurnover,
    int employeesCount,
    Double rating,
    String fullName,
    OrganizationType type,
    AddressReadDto postalAddressId
    // formatter:on
    ) {

  public static OrganizationReadDto toReadDtoForRead(Organization organization) {
    return new OrganizationReadDto(
        organization.getId(),
        organization.getName(),
        CoordinatesReadDto.toReadDtoForRead(organization.getCoordinates()),
        organization.getCreationDate(),
        AddressReadDto.toReadDtoForRead(organization.getOfficialAddress()),
        organization.getAnnualTurnover(),
        organization.getEmployeesCount(),
        organization.getRating(),
        organization.getFullName(),
        organization.getType(),
        AddressReadDto.toReadDtoForRead(organization.getPostalAddress()));
  }
}
