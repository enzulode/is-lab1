package com.enzulode.exception.proposal;

import java.io.Serial;

public abstract class ProposalException extends RuntimeException {

  @Serial private static final long serialVersionUID = 9841723719823L;

  public ProposalException(String message) {
    super(message);
  }

  public ProposalException(String message, Throwable cause) {
    super(message, cause);
  }
}
