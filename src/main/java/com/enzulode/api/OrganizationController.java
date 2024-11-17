package com.enzulode.api;

import com.enzulode.dto.OrganizationMutationDto;
import com.enzulode.dto.OrganizationReadDto;
import com.enzulode.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

  private final OrganizationService organizationService;

  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrganizationReadDto createEndpoint(@RequestBody OrganizationMutationDto mutationDto) {
    return organizationService.create(mutationDto);
  }

  @GetMapping
  public Page<OrganizationReadDto> readEndpoint(Pageable pageable) {
    return organizationService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public OrganizationReadDto updateEndpoint(
      @PathVariable Integer id, @RequestBody OrganizationMutationDto mutationDto) {
    return organizationService.update(id, mutationDto);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    organizationService.delete(id);
  }
}
