package com.enzulode.api;

import com.enzulode.dto.ProposalReadDto;
import com.enzulode.service.AdminProposalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/proposal")
public class AdminProposalController {

  private final AdminProposalService proposalService;

  public AdminProposalController(AdminProposalService proposalService) {
    this.proposalService = proposalService;
  }

  @PostMapping
  public void promotionEndpoint() {
    proposalService.promote();
  }

  @GetMapping
  public Page<ProposalReadDto> readEndpoint(Pageable pageable) {
    return proposalService.findAll(pageable);
  }

  @PatchMapping("/accept/{id}")
  public void acceptProposalEndpoint(@PathVariable Long id) {
    proposalService.accept(id);
  }

  @PatchMapping("/decline/{id}")
  public void declineProposalEndpoint(@PathVariable Long id) {
    proposalService.decline(id);
  }
}
