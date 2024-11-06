package com.enzulode.exception;

import java.io.Serial;

public class UnauthorizedOperationException extends RuntimeException {

  @Serial private static final long serialVersionUID = -524412312123L;

  public UnauthorizedOperationException(String message) {
    super(message);
  }

  public UnauthorizedOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
