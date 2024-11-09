package com.enzulode.dto;

import com.enzulode.dao.entity.Coordinates;

public record CoordinatesReadDto(Long id, Float x, int y) {

  public static CoordinatesReadDto toReadDtoForRead(Coordinates coordinatesEntity) {
    return new CoordinatesReadDto(
        coordinatesEntity.getId(), coordinatesEntity.getX(), coordinatesEntity.getY());
  }
}
