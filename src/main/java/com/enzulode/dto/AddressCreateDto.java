package com.enzulode.dto;

import jakarta.validation.constraints.NotNull;

public record AddressCreateDto(
    String street, @NotNull(message = "Address townId should be specified.") Long townId) {}
