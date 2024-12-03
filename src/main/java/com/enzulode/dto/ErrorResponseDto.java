package com.enzulode.dto;

import java.util.List;

public record ErrorResponseDto(List<String> messages) {

  public ErrorResponseDto(String message) {
    this(List.of(message));
  }
}
