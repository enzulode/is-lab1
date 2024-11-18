package com.enzulode.exception.patch;

import java.io.Serial;

public class PatchIdMismatchException extends PatchException {

  @Serial private static final long serialVersionUID = 827482348L;

  public PatchIdMismatchException(String message) {
    super(message);
  }

  public PatchIdMismatchException(String message, Throwable cause) {
    super(message, cause);
  }
}
