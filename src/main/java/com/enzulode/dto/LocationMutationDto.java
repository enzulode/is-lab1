package com.enzulode.dto;

import com.enzulode.dao.entity.Location;

public record LocationMutationDto(Float x, double y, Long z) {

  public static Location toEntityForCreate(LocationMutationDto mutationDto) {
    return new Location(mutationDto.x(), mutationDto.y(), mutationDto.z());
  }

  public static Location toEntityForUpdate(
      Location existingLocation, LocationMutationDto mutationDto) {
    existingLocation.setX(mutationDto.x());
    existingLocation.setY(mutationDto.y());
    existingLocation.setZ(mutationDto.z());
    return existingLocation;
  }
}
