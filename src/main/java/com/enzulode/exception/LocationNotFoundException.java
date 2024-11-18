package com.enzulode.exception;

import java.io.Serial;

public class LocationNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -463573624L;

  public LocationNotFoundException() {
    super("Location not found");
  }

  public LocationNotFoundException(String message) {
    super(message);
  }

  public LocationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
