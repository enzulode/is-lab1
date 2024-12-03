package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

@Entity
public class Organization extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq")
  @SequenceGenerator(name = "organization_seq")
  private Integer id;

  @NotNull @NotBlank private String name;

  @ManyToOne
  @JoinColumn(name = "coordinates_id")
  @NotNull private Coordinates coordinates;

  @NotNull private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "official_address_id")
  @NotNull private Address officialAddress;

  @Positive private int annualTurnover;

  @Positive private int employeesCount;

  @Positive private Double rating;

  @NotNull @Size(max = 1658)
  private String fullName;

  @Enumerated(EnumType.STRING)
  private OrganizationType type;

  @ManyToOne
  @JoinColumn(name = "postal_address_id")
  @NotNull private Address postalAddress;

  public Organization() {}

  public Organization(
      String name,
      Coordinates coordinates,
      LocalDateTime creationDate,
      Address officialAddress,
      int annualTurnover,
      int employeesCount,
      Double rating,
      String fullName,
      OrganizationType type,
      Address postalAddress) {
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.officialAddress = officialAddress;
    this.annualTurnover = annualTurnover;
    this.employeesCount = employeesCount;
    this.rating = rating;
    this.fullName = fullName;
    this.type = type;
    this.postalAddress = postalAddress;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Address getOfficialAddress() {
    return officialAddress;
  }

  public void setOfficialAddress(Address officialAddress) {
    this.officialAddress = officialAddress;
  }

  public int getAnnualTurnover() {
    return annualTurnover;
  }

  public void setAnnualTurnover(int annualTurnover) {
    this.annualTurnover = annualTurnover;
  }

  public int getEmployeesCount() {
    return employeesCount;
  }

  public void setEmployeesCount(int employeesCount) {
    this.employeesCount = employeesCount;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public OrganizationType getType() {
    return type;
  }

  public void setType(OrganizationType type) {
    this.type = type;
  }

  public Address getPostalAddress() {
    return postalAddress;
  }

  public void setPostalAddress(Address postalAddress) {
    this.postalAddress = postalAddress;
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
    Organization that = (Organization) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
