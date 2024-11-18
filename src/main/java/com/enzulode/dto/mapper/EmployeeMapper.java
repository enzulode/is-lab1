package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Employee;
import com.enzulode.dto.EmployeeCreateDto;
import com.enzulode.dto.EmployeeReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "organizations", ignore = true)
  Employee toEntity(EmployeeCreateDto createDto);

  EmployeeReadDto toReadDto(Employee employee);
}
