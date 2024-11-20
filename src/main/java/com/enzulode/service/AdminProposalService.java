package com.enzulode.service;

import com.enzulode.dto.ProposalReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProposalService {

  void promote();

  Page<ProposalReadDto> findAll(Pageable pageable);

  void accept(Long proposalId);

  void decline(Long proposalId);
}
