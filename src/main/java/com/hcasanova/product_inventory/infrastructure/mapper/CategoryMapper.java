package com.hcasanova.product_inventory.infrastructure.mapper;

import com.hcasanova.product_inventory.domain.model.Category;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.rest.dto.CategoryDTO;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryMapper {

  @Inject ProductMapper productMapper;

  public Category toEntity(CategoryDTO dto) {
    Category category = new Category();
    category.name = dto.name;

    if (dto.products != null) {
      category.getProducts().clear();
      for (ProductDTO pDto : dto.products) {
        Product p = productMapper.toEntity(pDto);
        p.category = category;
        category.getProducts().add(p);
      }
    }

    return category;
  }

  public CategoryDTO toDTO(Category category) {
    CategoryDTO dto = new CategoryDTO();
    dto.id = category.id;
    dto.name = category.name;
    if (category.getProducts() != null) {
      dto.products =
          category.getProducts().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
    return dto;
  }

  public List<CategoryDTO> toDTOList(List<Category> categories) {
    return categories.stream().map(this::toDTO).collect(Collectors.toList());
  }
}
