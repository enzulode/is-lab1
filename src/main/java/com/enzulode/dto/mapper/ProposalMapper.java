package com.enzulode.dto.mapper;

import com.enzulode.dao.entity.Proposal;
import com.enzulode.dto.ProposalReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProposalMapper {

  ProposalReadDto toReadDto(Proposal proposal);
}
