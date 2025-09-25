package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;

@Entity
public class Product extends PanacheEntity {

  public String name;
  public String description;
  public Double price;
  public Integer quantity;

  @Version public Long version;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = true)
  public Category category;

  // Getters to avoid conflict when accessing property from service
  public Long getVersion() {
    return this.version;
  }
}
