package com.enzulode.api;

import com.enzulode.dto.OrganizationMutationDto;
import com.enzulode.dto.OrganizationReadDto;
import com.enzulode.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public Page<OrganizationReadDto> readEndpoint(
      @RequestParam("page") int pageNum,
      @RequestParam("pageSize") int pageSize,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
    if (sortBy != null && !sortBy.isBlank()) {
      Sort.Direction dir = Sort.Direction.fromString(order);
      Sort sort = Sort.by(dir, sortBy);
      Pageable page = PageRequest.of(pageNum, pageSize, sort);
      return organizationService.findAll(page);
    }

    Pageable page = PageRequest.of(pageNum, pageSize);
    return organizationService.findAll(page);
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
