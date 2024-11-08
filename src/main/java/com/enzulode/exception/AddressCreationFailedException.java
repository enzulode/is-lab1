package com.enzulode.exception;

import java.io.Serial;

public class AddressCreationFailedException extends RuntimeException {

  @Serial private static final long serialVersionUID = 712678126378L;

  public AddressCreationFailedException(String message) {
    super(message);
  }

  public AddressCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
