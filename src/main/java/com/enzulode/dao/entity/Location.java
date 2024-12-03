package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

@Entity
public class Location extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
  @SequenceGenerator(name = "location_seq")
  private Long id;

  @NotNull private Float x;

  private double y;

  @NotNull private Long z;

  public Location() {}

  public Location(Float x, double y, Long z) {
    this.x = x;
    this.y = y;
    this.z = z;
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

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public Long getZ() {
    return z;
  }

  public void setZ(Long z) {
    this.z = z;
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
    Location location = (Location) o;
    return getId() != null && Objects.equals(getId(), location.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
