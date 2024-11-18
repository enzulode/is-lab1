package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Coordinates;
import com.enzulode.dto.CoordinatesCreateDto;
import com.enzulode.dto.CoordinatesReadDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinatesMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "organizations", ignore = true)
  Coordinates toEntity(CoordinatesCreateDto createDto);

  CoordinatesReadDto toReadDto(Coordinates entity);
}
