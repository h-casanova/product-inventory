package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public List<Product> listAll(int pageIndex, int pageSize) {    	
        return pageIndex <= 0 && pageSize == 0
            ? productRepository.listAll() 
            : productRepository.findAll()
                        .page(Page.of(pageIndex, pageSize))
                        .list();
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
                "Version mismatch: expected " + existing.getVersion() + " but got " + dto.version
            );
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
