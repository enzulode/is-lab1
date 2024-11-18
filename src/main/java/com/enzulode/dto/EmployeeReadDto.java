package com.enzulode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record EmployeeReadDto(
    Long id,
    String firstName,
    String lastName,
    String middleName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate birthDate) {}
