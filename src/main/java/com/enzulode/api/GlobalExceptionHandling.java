package com.enzulode.api;

import com.enzulode.dto.ErrorResponseDto;
import com.enzulode.exception.*;
import com.enzulode.exception.patch.PatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

  @ExceptionHandler({
    CoordinatesNotFoundException.class,
    LocationNotFoundException.class,
    AddressNotFoundException.class,
    OrganizationNotFoundException.class
  })
  public ResponseEntity<ErrorResponseDto> handleCoordinatesNotFoundFailure(RuntimeException cause) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(cause.getMessage()));
  }

  @ExceptionHandler({UnauthorizedOperationException.class})
  public ResponseEntity<ErrorResponseDto> handleUnauthorizedOperationException(
      RuntimeException cause) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponseDto(cause.getMessage()));
  }

  @ExceptionHandler({PatchException.class})
  public ResponseEntity<ErrorResponseDto> handlePatchException(PatchException cause) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDto(cause.getMessage()));
  }
}
