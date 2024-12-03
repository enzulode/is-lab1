package com.enzulode.dto;

import com.enzulode.dao.entity.OrganizationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record OrganizationCreateDto(
    // formatter:off
    @NotNull(message = "Organization name should be specified") @NotBlank(message = "Organization name cannot be blank") String name,
    @NotNull(message = "Organization coordinatesId should be specified") Long coordinatesId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Organization creationDate should be specified") LocalDateTime creationDate,
    @NotNull(message = "Organization officialAddressId should be specified") Long officialAddressId,
    @Positive(message = "Organization annualTurnover should remain positive") int annualTurnover,
    @Positive(message = "Organization employeesCount should remain positive") int employeesCount,
    @Positive(message = "Organization rating should remain positive") Double rating,
    @NotNull(message = "Organization fullName should be specified") @Size(max = 1658, message = "Organization fullName cannot be longer than 1658 characters") String fullName,
    OrganizationType type,
    @NotNull(message = "Organization postalAddressId should be specified") Long postalAddressId
    // formatter:on
    ) {}
