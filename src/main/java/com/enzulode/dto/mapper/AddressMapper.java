package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Address;
import com.enzulode.dto.AddressCreateDto;
import com.enzulode.dto.AddressReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "town.id", source = "townId")
  @Mapping(target = "organizationsOfficialAddresses", ignore = true)
  @Mapping(target = "organizationsPostalAddresses", ignore = true)
  Address toEntity(AddressCreateDto createDto);

  AddressReadDto toReadDto(Address address);
}
