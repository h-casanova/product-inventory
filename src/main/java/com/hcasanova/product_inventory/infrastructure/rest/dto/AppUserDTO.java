package com.hcasanova.product_inventory.infrastructure.rest.dto;

public class AppUserDTO {

  public Long id;
  public String username;
  public String role;

  public AppUserDTO() {}

  public AppUserDTO(Long id, String username, String role) {
    this.id = id;
    this.username = username;
    this.role = role;
  }
}
