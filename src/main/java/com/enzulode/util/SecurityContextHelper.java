package com.enzulode.util;

import com.enzulode.exception.UnauthorizedOperationException;
import java.security.Principal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

  public String findUserName() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    // formatter:off
    if (principal.getName() == null)
      throw new UnauthorizedOperationException(
          "Unable to perform operation: no information about the authentication is present."
      );
    return principal.getName();
    // formatter:on
  }
}
