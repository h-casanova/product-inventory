package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    public List<Product> listAll() {
        return repository.listAll();
    }

    public Product findById(Long id) {
        return repository.findById(id);
    }
}
