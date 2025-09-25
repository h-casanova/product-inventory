package com.hcasanova.product_inventory.infrastructure.persistence.repository;

import com.hcasanova.product_inventory.domain.model.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {
  // Custom queries
}
