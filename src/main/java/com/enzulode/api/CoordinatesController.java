package com.enzulode.api;

import com.enzulode.dto.CoordinatesMutationDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.service.CoordinatesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coordinates")
public class CoordinatesController {

  private final CoordinatesService coordinatesService;

  public CoordinatesController(CoordinatesService coordinatesService) {
    this.coordinatesService = coordinatesService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CoordinatesReadDto createEndpoint(@RequestBody CoordinatesMutationDto mutationDto) {
    return coordinatesService.create(mutationDto);
  }

  @GetMapping
  public Page<CoordinatesReadDto> readEndpoint(Pageable pageable) {
    return coordinatesService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public CoordinatesReadDto updateEndpoint(
      @PathVariable Long id, @RequestBody CoordinatesMutationDto mutationDto) {
    return coordinatesService.update(id, mutationDto);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    coordinatesService.delete(id);
  }
}
