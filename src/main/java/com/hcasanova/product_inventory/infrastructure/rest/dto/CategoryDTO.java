package com.hcasanova.product_inventory.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CategoryDTO {
  public Long id;

  @NotBlank(message = "Category name is required")
  public String name;

  public List<ProductDTO> products;
}
