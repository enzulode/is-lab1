package com.enzulode.api;

import com.enzulode.dto.CoordinatesMutationDto;
import com.enzulode.dto.CoordinatesReadDto;
import com.enzulode.service.CoordinatesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public Page<CoordinatesReadDto> readEndpoint(
      @RequestParam("page") int pageNum,
      @RequestParam("pageSize") int pageSize,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
    if (sortBy != null && !sortBy.isBlank()) {
      Sort.Direction dir = Sort.Direction.fromString(order);
      Sort sort = Sort.by(dir, sortBy);
      Pageable page = PageRequest.of(pageNum, pageSize, sort);
      return coordinatesService.findAll(page);
    }

    Pageable page = PageRequest.of(pageNum, pageSize);
    return coordinatesService.findAll(page);
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
