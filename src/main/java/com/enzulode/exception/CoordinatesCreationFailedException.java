package com.enzulode.exception;

import java.io.Serial;

public class CoordinatesCreationFailedException extends RuntimeException {

  @Serial private static final long serialVersionUID = 12731892731829L;

  public CoordinatesCreationFailedException(String message) {
    super(message);
  }

  public CoordinatesCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
