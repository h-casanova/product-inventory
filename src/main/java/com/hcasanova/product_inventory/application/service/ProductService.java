package com.hcasanova.product_inventory.application.service;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.ProductRepository;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    public List<Product> listAll(int pageIndex, int pageSize) {
    	System.out.println("Paginating: page=" + pageIndex + ", size=" + pageSize);
    	
        return pageSize <= 0 && pageIndex == 0
            ? repository.listAll() 
            : repository.findAll()
                        .page(Page.of(pageIndex, pageSize))
                        .list();
    }


    public Product findById(Long id) {
        return repository.findById(id);
    }
}
