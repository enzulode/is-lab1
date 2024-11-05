package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;

@Entity
public class Address extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String street;

  @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Location town;

  public Address() {}

  public Address(String street, Location town) {
    this.street = street;
    this.town = town;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Location getTown() {
    return town;
  }

  public void setTown(Location town) {
    this.town = town;
  }
}
