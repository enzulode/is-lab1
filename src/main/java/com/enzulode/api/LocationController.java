package com.enzulode.api;

import com.enzulode.dto.LocationMutationDto;
import com.enzulode.dto.LocationReadDto;
import com.enzulode.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

  private final LocationService locationService;

  public LocationController(LocationService locationService) {
    this.locationService = locationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public LocationReadDto createEndpoint(@RequestBody LocationMutationDto mutationDto) {
    return locationService.create(mutationDto);
  }

  @GetMapping
  public Page<LocationReadDto> readEndpoint(Pageable pageable) {
    return locationService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public LocationReadDto updateEndpoint(
      @PathVariable Long id, @RequestBody LocationMutationDto mutationDto) {
    return locationService.update(id, mutationDto);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    locationService.delete(id);
  }
}
