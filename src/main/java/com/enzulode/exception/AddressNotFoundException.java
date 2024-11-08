package com.enzulode.exception;

import java.io.Serial;

public class AddressNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -863427841234L;

  public AddressNotFoundException(String message) {
    super(message);
  }

  public AddressNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
