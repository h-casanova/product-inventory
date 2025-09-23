package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public List<Product> listAll(int pageIndex, int pageSize) {    	
        return pageSize <= 0 && pageIndex == 0
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
}
