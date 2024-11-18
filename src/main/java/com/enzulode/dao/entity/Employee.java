package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

@Entity
public class Employee extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
  @SequenceGenerator(name = "employee_seq")
  private Long id;

  private String firstName;

  private String lastName;

  private String middleName;

  private LocalDate birthDate;

  public Employee() {}

  public Employee(String firstName, String lastName, String middleName, LocalDate birthDate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.middleName = middleName;
    this.birthDate = birthDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
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
    Employee employee = (Employee) o;
    return getId() != null && Objects.equals(getId(), employee.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
