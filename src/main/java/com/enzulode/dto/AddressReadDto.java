package com.enzulode.dto;

import com.enzulode.dao.entity.Address;

public record AddressReadDto(Long id, String street, LocationReadDto town) {

  public static AddressReadDto toReadDtoForRead(Address address) {
    return new AddressReadDto(
        address.getId(), address.getStreet(), LocationReadDto.toReadDtoForRead(address.getTown()));
  }
}
