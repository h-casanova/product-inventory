package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import jakarta.persistence.Version;

@Entity
public class Product extends PanacheEntity {
	
    public String name;
    public String description;
    public Double price;
    public Integer quantity;

    @Version
    public Long version;

    public Long getVersion() {
    	return this.version;
    }
    
}
