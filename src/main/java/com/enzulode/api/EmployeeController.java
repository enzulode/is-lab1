package com.enzulode.api;

import com.enzulode.dto.EmployeeCreateDto;
import com.enzulode.dto.EmployeeReadDto;
import com.enzulode.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EmployeeReadDto createEndpoint(@RequestBody EmployeeCreateDto createDto) {
    return employeeService.create(createDto);
  }

  @GetMapping
  public Page<EmployeeReadDto> readEndpoint(Pageable pageable) {
    return employeeService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public EmployeeReadDto updateEndpoint(@PathVariable Long id, @RequestBody JsonNode patchNode) {
    return employeeService.update(id, patchNode);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    employeeService.delete(id);
  }
}
