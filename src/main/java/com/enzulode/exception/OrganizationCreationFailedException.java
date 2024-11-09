package com.enzulode.exception;

import java.io.Serial;

public class OrganizationCreationFailedException extends RuntimeException {

  @Serial private static final long serialVersionUID = 23521412412L;

  public OrganizationCreationFailedException(String message) {
    super(message);
  }

  public OrganizationCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
