package com.enzulode.exception;

import java.io.Serial;

public class CoordinatesNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 32937189237812L;

  public CoordinatesNotFoundException() {
    super("Coordinates not found");
  }

  public CoordinatesNotFoundException(String message) {
    super(message);
  }

  public CoordinatesNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
