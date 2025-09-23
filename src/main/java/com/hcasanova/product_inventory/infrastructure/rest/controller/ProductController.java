package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.ProductService;
import com.hcasanova.product_inventory.domain.model.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService service;

    @GET
    public List<Product> getAll(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("0") int size
        ) {
            return service.listAll(page, size);
        }

    @GET
    @Path("/{id}")
    public Product getById(@PathParam("id") Long id) {
        Product product = service.findById(id);
        if (product == null) throw new NotFoundException("Product not found");
        return product;
    }
}
