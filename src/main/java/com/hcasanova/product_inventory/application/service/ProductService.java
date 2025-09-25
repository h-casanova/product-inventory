package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class ProductService {

  @Inject ProductRepository productRepository;

  public List<Product> listAll(
      int page,
      int size,
      String name,
      Double minPrice,
      Double maxPrice,
      String sortBy,
      boolean sortAsc) {

    if (page < 0 || size < 0) throw new BadRequestException("Page and size must be >= 0");

    StringBuilder jpql = new StringBuilder("FROM Product p WHERE 1=1");

    // Filters
    if (name != null && !name.isBlank()) {
      jpql.append(" AND p.name LIKE :name");
    }
    if (minPrice != null) {
      jpql.append(" AND p.price >= :minPrice");
    }
    if (maxPrice != null) {
      jpql.append(" AND p.price <= :maxPrice");
    }

    // Order by name or price
    if (sortBy != null) {
      if (!sortBy.equals("name") && !sortBy.equals("price"))
        throw new BadRequestException("Invalid sort field");
      jpql.append(" ORDER BY p.").append(sortBy).append(sortAsc ? " ASC" : " DESC");
    }

    // Query arrangement and applying filters
    var query = productRepository.getEntityManager().createQuery(jpql.toString(), Product.class);
    if (name != null && !name.isBlank()) {
      query.setParameter("name", "%" + name + "%");
    }
    if (minPrice != null) {
      query.setParameter("minPrice", minPrice);
    }
    if (maxPrice != null) {
      query.setParameter("maxPrice", maxPrice);
    }

    // Full list or pagination
    if (page == 0 && size == 0) {
      return query.getResultList();
    } else {
      query.setFirstResult(page * size);
      query.setMaxResults(size);
      return query.getResultList();
    }
  }

  public Product findById(Long id) {
    return productRepository.findById(id);
  }

  @Transactional
  public Product createSingleProduct(Product product) {
    productRepository.persist(product);
    return product;
  }

  @Transactional
  public List<Product> createBulkProducts(List<Product> products) {
    for (Product product : products) {
      productRepository.persist(product);
    }
    return products;
  }

  @Transactional
  public Product updateProduct(Long id, ProductDTO dto) {
    Product existing = productRepository.findById(id);
    if (existing == null) {
      throw new NotFoundException("Product not found");
    }

    if (!dto.version.equals(existing.getVersion())) {
      throw new OptimisticLockException(
          "Version mismatch: expected " + existing.getVersion() + " but got " + dto.version);
    }

    existing.name = dto.name;
    existing.description = dto.description;
    existing.price = dto.price;
    existing.quantity = dto.quantity;

    return existing;
  }

  @Transactional
  public boolean deleteProduct(Long id) {
    Product existing = productRepository.findById(id);
    if (existing == null) {
      return false;
    }
    productRepository.delete(existing);
    return true;
  }
}
