package com.enzulode.exception.patch;

import java.io.Serial;

public class PatchFailedException extends PatchException {

  @Serial private static final long serialVersionUID = -98772384213L;

  public PatchFailedException(String message) {
    super(message);
  }

  public PatchFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
