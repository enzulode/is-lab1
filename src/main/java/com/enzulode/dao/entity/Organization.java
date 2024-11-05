package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Organization extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Coordinates coordinates;

  private LocalDateTime creationDate;

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Address officialAddress;

  private int annualTurnover;

  private int employeesCount;

  private Double rating;

  private String fullName;

  @Enumerated(EnumType.STRING)
  private OrganizationType type;

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Address postalAddress;

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
}
