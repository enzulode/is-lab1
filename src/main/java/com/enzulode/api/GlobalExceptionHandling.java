package com.enzulode.api;

import com.enzulode.dto.ErrorResponseDto;
import com.enzulode.exception.CoordinatesCreationFailedException;
import com.enzulode.exception.CoordinatesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandling {

  @ExceptionHandler({CoordinatesCreationFailedException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleCoordinatesCreationFailure(Throwable cause) {
    return new ErrorResponseDto(cause.getMessage());
  }

  @ExceptionHandler({CoordinatesNotFoundException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleCoordinatesNotFoundFailure(Throwable cause) {
    return new ErrorResponseDto(cause.getMessage());
  }
}