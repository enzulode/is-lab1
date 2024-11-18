package com.enzulode.exception;

import java.io.Serial;

public class EmployeeNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 9781273193791829L;

  public EmployeeNotFoundException() {
    super("Employee not found");
  }

  public EmployeeNotFoundException(String message) {
    super(message);
  }

  public EmployeeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
