package com.hcasanova.product_inventory.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class AppUserDTO {
  public Long id;

  @NotBlank(message = "Username name is required")
  public String username;

  @NotBlank(message = "Role name is required")
  public String role;

  public AppUserDTO() {}

  public AppUserDTO(Long id, String username, String role) {
    this.id = id;
    this.username = username;
    this.role = role;
  }
}
