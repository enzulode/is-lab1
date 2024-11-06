package com.enzulode.exception;

import java.io.Serial;

public class LocationCreationFailedException extends RuntimeException {

  @Serial private static final long serialVersionUID = 7231463138L;

  public LocationCreationFailedException(String message) {
    super(message);
  }

  public LocationCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
