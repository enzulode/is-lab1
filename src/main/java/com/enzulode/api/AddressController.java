package com.enzulode.api;

import com.enzulode.dto.AddressMutationDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.service.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

  private final AddressService addressService;

  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AddressReadDto createEndpoint(@RequestBody AddressMutationDto mutationDto) {
    return addressService.create(mutationDto);
  }

  @GetMapping
  public Page<AddressReadDto> readEndpoint(
      @RequestParam("page") int pageNum,
      @RequestParam("pageSize") int pageSize,
      @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
    if (sortBy != null && !sortBy.isBlank()) {
      Sort.Direction dir = Sort.Direction.fromString(order);
      Sort sort = Sort.by(dir, sortBy);
      Pageable page = PageRequest.of(pageNum, pageSize, sort);
      return addressService.findAll(page);
    }

    Pageable page = PageRequest.of(pageNum, pageSize);
    return addressService.findAll(page);
  }

  @PatchMapping("/{id}")
  public AddressReadDto updateEndpoint(
      @PathVariable Long id, @RequestBody AddressMutationDto mutationDto) {
    return addressService.update(id, mutationDto);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    addressService.delete(id);
  }
}
