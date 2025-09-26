package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "APP_USER")
public class AppUser extends PanacheEntity {

  @NotBlank(message = "username name is required")
  private String username;

  @NotBlank(message = "password name is required")
  private String passwordHash;

  @NotBlank(message = "role name is required")
  private String role;

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getRole() {
    return role;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
