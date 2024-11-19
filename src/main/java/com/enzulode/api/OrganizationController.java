package com.enzulode.api;

import com.enzulode.dto.*;
import com.enzulode.service.OrganizationService;
import com.fasterxml.jackson.databind.JsonNode;
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
  public OrganizationReadDto createEndpoint(@RequestBody OrganizationCreateDto mutationDto) {
    return organizationService.create(mutationDto);
  }

  @GetMapping
  public Page<OrganizationReadDto> readEndpoint(Pageable pageable) {
    return organizationService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public OrganizationReadDto updateEndpoint(
      @PathVariable Integer id, @RequestBody JsonNode patchNode) {
    return organizationService.update(id, patchNode);
  }

  @PatchMapping("/{organizationId}/coordinates/{coordinatesId}")
  public OrganizationReadDto updateOrganizationCoordinatesEndpoint(
      @PathVariable Integer organizationId, @PathVariable Long coordinatesId) {
    return organizationService.updateCoordinates(organizationId, coordinatesId);
  }

  @PatchMapping("/{organizationId}/address/official/{addressId}")
  public OrganizationReadDto updateOrganizationOfficialAddressEndpoint(
      @PathVariable Integer organizationId, @PathVariable Long addressId) {
    return organizationService.updateOfficialAddress(organizationId, addressId);
  }

  @PatchMapping("/{organizationId}/address/postal/{addressId}")
  public OrganizationReadDto updateOrganizationPostalAddressEndpoint(
      @PathVariable Integer organizationId, @PathVariable Long addressId) {
    return organizationService.updatePostalAddress(organizationId, addressId);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Integer id) {
    organizationService.delete(id);
  }

  @GetMapping("/rating/total")
  public OrganizationsTotalRatingDto totalRatingEndpoint() {
    return organizationService.totalRating();
  }

  @GetMapping("/name/less")
  public CountOrganizationFullNameLessThanDto countOrganizationsFullNameLessThanEndpoint(
      @RequestParam("name") String name) {
    return organizationService.countOrganizationFullNameLessThan(name);
  }

  @GetMapping("/name/more")
  public CountOrganizationFullNameMoreThanDto countOrganizationFullNameMoreThanEndpoint(
      @RequestParam("name") String name) {
    return organizationService.countOrganizationFullNameMoreThan(name);
  }
}
