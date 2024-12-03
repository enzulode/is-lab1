package com.enzulode.api;

import com.enzulode.dto.CoordinatesCreateDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.service.CoordinatesService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
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
  public CoordinatesReadDto createEndpoint(@RequestBody @Valid CoordinatesCreateDto createDto) {
    return coordinatesService.create(createDto);
  }

  @GetMapping
  public Page<CoordinatesReadDto> readEndpoint(Pageable pageable) {
    return coordinatesService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public CoordinatesReadDto updateEndpoint(@PathVariable Long id, @RequestBody JsonNode patchNode) {
    return coordinatesService.update(id, patchNode);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    coordinatesService.delete(id);
  }
}
