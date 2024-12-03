package com.enzulode.dto;

import jakarta.validation.constraints.NotNull;

public record LocationCreateDto(
    @NotNull(message = "Location x coordinate should be specified.") Float x,
    double y,
    @NotNull(message = "Location z coordinate should be specified.") Long z) {}
