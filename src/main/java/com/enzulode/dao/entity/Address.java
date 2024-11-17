package com.enzulode.dao.entity;

import com.enzulode.dao.entity.common.BusinessEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Address extends BusinessEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String street;

  @OneToOne(optional = false, fetch = FetchType.EAGER)
  private Location town;

  @OneToMany(mappedBy = "officialAddress")
  private Collection<Organization> organizationsOfficialAddresses = new ArrayList<>();

  @OneToMany(mappedBy = "postalAddress")
  private Collection<Organization> organizationsPostalAddresses = new ArrayList<>();

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

  public Collection<Organization> getOrganizationsOfficialAddresses() {
    return organizationsOfficialAddresses;
  }

  public void setOrganizationsOfficialAddresses(
      Collection<Organization> organizationsOfficialAddresses) {
    this.organizationsOfficialAddresses = organizationsOfficialAddresses;
  }

  public Collection<Organization> getOrganizationsPostalAddresses() {
    return organizationsPostalAddresses;
  }

  public void setOrganizationsPostalAddresses(
      Collection<Organization> organizationsPostalAddresses) {
    this.organizationsPostalAddresses = organizationsPostalAddresses;
  }
}
