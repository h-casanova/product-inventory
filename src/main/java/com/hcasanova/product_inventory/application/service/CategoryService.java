package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Category;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.CategoryRepository;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;
import com.hcasanova.product_inventory.infrastructure.rest.dto.CategoryDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class CategoryService {

  @Inject CategoryRepository categoryRepository;

  @Inject ProductRepository productRepository;

  public List<Category> listAll() {
    return categoryRepository.listAll();
  }

  public Category findById(Long id) {
    Category category = categoryRepository.findById(id);
    if (category == null) throw new NotFoundException("Category not found");
    return category;
  }

  @Transactional
  public Category createCategory(Category category) {
    categoryRepository.persist(category);
    return category;
  }

  @Transactional
  public Category updateCategory(Long id, @Valid CategoryDTO dto) {
    Category category = categoryRepository.findById(id);
    if (category == null) {
      throw new NotFoundException("Category not found");
    }
    category.name = dto.name;
    category.products.size();
    return category;
  }

  @Transactional
  public boolean deleteCategory(Long id) {
    Category category = categoryRepository.findById(id);
    if (category == null) {
      return false;
    }

    for (Product product : category.getProducts()) {
      product.category = null;
      productRepository.persist(product);
    }

    categoryRepository.delete(category);
    return true;
  }

  @Transactional
  public Category addProduct(Long categoryId, Long productId) {
    Category category = findById(categoryId);
    Product product = productRepository.findById(productId);

    if (product == null) {
      throw new NotFoundException("Product not found");
    }

    category.addProduct(product);

    category.getProducts().size();

    return category;
  }

  @Transactional
  public Category removeProduct(Long categoryId, Long productId) {
    Category category = findById(categoryId);
    Product product = productRepository.findById(productId);
    if (product == null) {
      throw new NotFoundException("Product not found");
    }
    category.removeProduct(product);
    return category;
  }
}
