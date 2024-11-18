package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

@Entity
public class Coordinates extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_seq")
  @SequenceGenerator(name = "coordinates_seq")
  private Long id;

  private Float x;

  private int y;

  @OneToMany(mappedBy = "coordinates")
  @JsonIgnore
  private Collection<Organization> organizations = new ArrayList<>();

  public Coordinates() {}

  public Coordinates(Float x, int y) {
    this.x = x;
    this.y = y;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getX() {
    return x;
  }

  public void setX(Float x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public Collection<Organization> getOrganizations() {
    return organizations;
  }

  public void setOrganizations(Collection<Organization> organizations) {
    this.organizations = organizations;
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
    Coordinates that = (Coordinates) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
