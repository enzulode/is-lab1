package com.enzulode.exception;

import java.io.Serial;

public class EntityCreationException extends RuntimeException {

  @Serial private static final long serialVersionUID = -19831792123313L;

  public EntityCreationException(String message) {
    super(message);
  }

  public EntityCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
