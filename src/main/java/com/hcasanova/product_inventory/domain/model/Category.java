package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends PanacheEntity {

  @NotBlank(message = "Category name is required")
  public String name;

  @OneToMany(mappedBy = "category")
  public List<Product> products = new ArrayList<>();

  public List<Product> getProducts() {
    return products;
  }

  // Methods

  public void addProduct(Product product) {
    products.add(product);
    product.category = this;
  }

  public void removeProduct(Product product) {
    products.remove(product);
    product.category = null;
  }
}
