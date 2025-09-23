package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Product extends PanacheEntity {
	
    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Description is required")
    public String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    public Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    public Integer quantity;

    @Version
    public Long version;
}
