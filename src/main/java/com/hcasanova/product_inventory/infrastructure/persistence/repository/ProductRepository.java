package com.hcasanova.product_inventory.infrastructure.persistence.repository;

import com.hcasanova.product_inventory.domain.model.Product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    // Reservado para consultas personalizadas
}
