package com.enzulode.dao.entity.common;

import com.enzulode.exception.EntityCreationException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
public abstract class BusinessEntity {

  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "modified_by", nullable = false)
  private String modifiedBy;

  @Column(name = "modified_at", nullable = false)
  private LocalDateTime modifiedAt;

  public String getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  @PrePersist
  public void defineCreatedByAndCreatedAt() {
    createdAt = LocalDateTime.now();
    createdBy = findUserName();
    modifiedAt = createdAt;
    modifiedBy = createdBy;
  }

  @PreUpdate
  public void defineModifiedByAndModifiedAt() {
    modifiedAt = LocalDateTime.now();
    modifiedBy = findUserName();
  }

  private String findUserName() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    if (principal == null) throw new EntityCreationException("Unable to define the object owner");

    return principal.getName();
  }
}
