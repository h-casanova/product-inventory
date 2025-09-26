package com.hcasanova.product_inventory.infrastructure.persistence.repository;

import com.hcasanova.product_inventory.domain.model.AppUser;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppUserRepository implements PanacheRepository<AppUser> {

  public AppUser findByUsername(String username) {
    return find("username", username).firstResult();
  }
}
