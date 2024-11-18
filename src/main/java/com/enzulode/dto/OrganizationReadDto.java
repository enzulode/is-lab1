package com.enzulode.dto;

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
    AddressReadDto postalAddress
    // formatter:on
    ) {}
