package com.enzulode.api;

import com.enzulode.dto.ErrorResponseDto;
import com.enzulode.exception.*;
import com.enzulode.exception.patch.PatchException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
      MethodArgumentNotValidException cause) {
    List<String> errorMessages =
        cause.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(errorMessages));
  }
}
