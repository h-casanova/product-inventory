package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.ProductService;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.mapper.ProductMapper;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/protected")
@Produces(MediaType.APPLICATION_JSON)
public class ProtectedRoutesController {

  @Inject ProductService productService;
  @Inject ProductMapper productMapper;

  @GET
  @Path("/admin/products")
  @Operation(summary = "List all products", description = "List all products for admin role")
  @RolesAllowed("admin")
  public List<ProductDTO> getAllProductsAdmin(@Context SecurityIdentity identity) {
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }

  @GET
  @Path("/user/products")
  @Operation(summary = "List all products", description = "List all products for user role")
  @RolesAllowed("user")
  public List<ProductDTO> getAllProductsUser(@Context SecurityIdentity identity) {
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }
}
