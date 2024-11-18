package com.enzulode.exception;

import java.io.Serial;

public class OrganizationNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -523523124L;

  public OrganizationNotFoundException() {
    super("Organization not found");
  }

  public OrganizationNotFoundException(String message) {
    super(message);
  }

  public OrganizationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
