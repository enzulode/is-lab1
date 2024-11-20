package com.enzulode.dto;

import com.enzulode.dao.entity.ProposalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ProposalReadDto(
    // formatter:off
    Long id,
    ProposalStatus status,
    String createdBy,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt,
    String modifiedBy,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime modifiedAt
    // formatter:on
    ) {}
