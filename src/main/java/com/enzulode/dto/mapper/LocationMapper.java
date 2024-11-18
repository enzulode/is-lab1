package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Location;
import com.enzulode.dto.LocationCreateDto;
import com.enzulode.dto.LocationReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

  @Mapping(target = "id", ignore = true)
  Location toEntity(LocationCreateDto createDto);

  LocationReadDto toReadDto(Location entity);
}
