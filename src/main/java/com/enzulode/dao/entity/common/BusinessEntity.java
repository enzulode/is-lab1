package com.enzulode.dao.entity.common;

import com.enzulode.exception.EntityCreationException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
public abstract class BusinessEntity {

  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public String getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @PrePersist
  public void defineCreatedByAndCreatedAt() {
    createdAt = LocalDateTime.now();
    createdBy = findUserName();
  }

  private String findUserName() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    if (principal == null) throw new EntityCreationException("Unable to define the object owner");

    return principal.getName();
  }
}
