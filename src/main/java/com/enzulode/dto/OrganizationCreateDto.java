package com.enzulode.dto;

import com.enzulode.dao.entity.OrganizationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record OrganizationCreateDto(
    // formatter:off
    String name,
    Long coordinatesId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime creationDate,
    Long officialAddressId,
    int annualTurnover,
    int employeesCount,
    Double rating,
    String fullName,
    OrganizationType type,
    Long postalAddressId
    // formatter:on
    ) {}
