package com.enzulode.api;

import com.enzulode.dto.ErrorResponseDto;
import com.enzulode.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandling {

  @ExceptionHandler({
    CoordinatesCreationFailedException.class,
    LocationCreationFailedException.class,
    AddressCreationFailedException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleCoordinatesCreationFailure(Throwable cause) {
    return new ErrorResponseDto(cause.getMessage());
  }

  @ExceptionHandler({
    CoordinatesNotFoundException.class,
    LocationNotFoundException.class,
    AddressNotFoundException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleCoordinatesNotFoundFailure(Throwable cause) {
    return new ErrorResponseDto(cause.getMessage());
  }
}
