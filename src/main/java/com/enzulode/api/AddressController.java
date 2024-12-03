package com.enzulode.api;

import com.enzulode.dto.AddressCreateDto;
import com.enzulode.dto.AddressReadDto;
import com.enzulode.service.AddressService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public AddressReadDto createEndpoint(@RequestBody @Valid AddressCreateDto mutationDto) {
    return addressService.create(mutationDto);
  }

  @GetMapping
  public Page<AddressReadDto> readEndpoint(Pageable pageable) {
    return addressService.findAll(pageable);
  }

  @PatchMapping("/{id}")
  public AddressReadDto updateEndpoint(@PathVariable Long id, @RequestBody JsonNode patchNode) {
    return addressService.update(id, patchNode);
  }

  @PatchMapping("/{addressId}/town/{townId}")
  public AddressReadDto updateAddressTown(@PathVariable Long addressId, @PathVariable Long townId) {
    return addressService.updateTown(addressId, townId);
  }

  @DeleteMapping("/{id}")
  public void deleteEndpoint(@PathVariable Long id) {
    addressService.delete(id);
  }
}
