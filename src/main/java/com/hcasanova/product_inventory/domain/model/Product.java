package com.hcasanova.product_inventory.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Product extends PanacheEntity {

    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Min(0)
    public Double price;

    @NotNull
    @Min(0)
    public Integer quantity;

    @Version
    public Long version;
}
