package com.hcasanova.product_inventory.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class CredentialsDTO {
  @NotBlank(message = "Username is required")
  public String username;

  @NotBlank(message = "A password is required")
  public String password;
}
