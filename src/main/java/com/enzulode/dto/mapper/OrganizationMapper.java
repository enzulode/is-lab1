package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Organization;
import com.enzulode.dto.OrganizationCreateDto;
import com.enzulode.dto.OrganizationReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "coordinates.id", source = "coordinatesId")
  @Mapping(target = "officialAddress.id", source = "officialAddressId")
  @Mapping(target = "postalAddress.id", source = "postalAddressId")
  Organization toEntity(OrganizationCreateDto createDto);

  OrganizationReadDto toReadDto(Organization organization);
}
