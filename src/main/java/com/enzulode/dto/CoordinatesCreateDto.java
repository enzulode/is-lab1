package com.enzulode.dto;

import jakarta.validation.constraints.NotNull;

public record CoordinatesCreateDto(
    @NotNull(message = "Coordinates x coordinate should be specified.") Float x, int y) {}
