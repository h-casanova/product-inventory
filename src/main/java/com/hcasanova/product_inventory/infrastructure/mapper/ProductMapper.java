package com.hcasanova.product_inventory.infrastructure.mapper;

import com.hcasanova.product_inventory.domain.model.Category;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductMapper {

  public Product toEntity(ProductDTO dto) {
    Product product = new Product();
    product.name = dto.name;
    product.description = dto.description;
    product.price = dto.price;
    product.quantity = dto.quantity;
    product.version = dto.version;

    if (dto.categoryId != null) {
      Category category = Category.findById(dto.categoryId);
      product.category = category;
    }

    return product;
  }

  public ProductDTO toDTO(Product product) {
    ProductDTO dto = new ProductDTO();
    dto.id = product.id;
    dto.name = product.name;
    dto.description = product.description;
    dto.price = product.price;
    dto.quantity = product.quantity;
    dto.version = product.version;

    if (product.category != null) {
      dto.categoryId = product.category.id;
      dto.categoryName = product.category.name;
    }

    return dto;
  }

  public List<ProductDTO> toDTOList(List<Product> products) {
    return products.stream().map(this::toDTO).collect(Collectors.toList());
  }
}
