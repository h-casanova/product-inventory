package com.hcasanova.product_inventory.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

  public Long id;

  @NotBlank(message = "Name is required")
  public String name;

  public String description;

  @NotNull(message = "Price is required")
  @Min(value = 0, message = "Price must be non-negative")
  public Double price;

  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity must be non-negative")
  public Integer quantity;

  public Long version;

  // Relation with category
  public Long categoryId;
  public String categoryName;
}
