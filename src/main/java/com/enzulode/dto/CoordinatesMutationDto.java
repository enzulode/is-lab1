package com.enzulode.dto;

import com.enzulode.dao.entity.Coordinates;

public record CoordinatesMutationDto(Float x, int y) {

  public static Coordinates toEntityForCreate(CoordinatesMutationDto mutationDto) {
    return new Coordinates(mutationDto.x(), mutationDto.y());
  }

  public static Coordinates toEntityForUpdate(
      Coordinates existingCoordinates, CoordinatesMutationDto mutationDto) {
    existingCoordinates.setX(mutationDto.x());
    existingCoordinates.setY(mutationDto.y());
    return existingCoordinates;
  }
}
