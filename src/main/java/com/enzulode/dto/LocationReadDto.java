package com.enzulode.dto;

import com.enzulode.dao.entity.Location;

public record LocationReadDto(Long id, Float x, double y, Long z) {

  public static LocationReadDto toReadDtoForRead(Location locationEntity) {
    return new LocationReadDto(
        locationEntity.getId(),
        locationEntity.getX(),
        locationEntity.getY(),
        locationEntity.getZ());
  }
}
