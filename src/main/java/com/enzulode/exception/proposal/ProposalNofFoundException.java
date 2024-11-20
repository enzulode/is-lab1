package com.enzulode.exception.proposal;

import java.io.Serial;

public class ProposalNofFoundException extends ProposalException {

  @Serial private static final long serialVersionUID = 8472342789423L;

  public ProposalNofFoundException() {
    super("Proposal not found");
  }

  public ProposalNofFoundException(String message) {
    super(message);
  }

  public ProposalNofFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
