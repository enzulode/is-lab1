package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

@Entity
public class Proposal extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proposal_seq")
  @SequenceGenerator(name = "proposal_seq")
  private Long id;

  @Enumerated(EnumType.STRING)
  private ProposalStatus status;

  private String userId;

  public Proposal() {}

  public Proposal(String userId, ProposalStatus status) {
    this.userId = userId;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProposalStatus getStatus() {
    return status;
  }

  public void setStatus(ProposalStatus status) {
    this.status = status;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String keycloakUserId) {
    this.userId = keycloakUserId;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    Proposal proposal = (Proposal) o;
    return getId() != null && Objects.equals(getId(), proposal.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
