package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "APP_USER")
public class AppUser extends PanacheEntity {

  private String username;
  private String passwordHash;
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
