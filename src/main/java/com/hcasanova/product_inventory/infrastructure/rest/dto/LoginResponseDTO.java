package com.hcasanova.product_inventory.infrastructure.rest.dto;

public class LoginResponseDTO {

  public String token;
  public AppUserDTO user;

  public LoginResponseDTO() {}

  public LoginResponseDTO(String token, AppUserDTO user) {
    this.token = token;
    this.user = user;
  }
}
