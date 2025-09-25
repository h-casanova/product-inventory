package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.ProductService;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.mapper.ProductMapper;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/protected")
@Produces(MediaType.APPLICATION_JSON)
public class ProtectedRoutesController {

  @Inject ProductService productService;
  @Inject ProductMapper productMapper;

  // ================= JWT LOCAL =================
  @GET
  @Path("/jwt/admin/products")
  public List<ProductDTO> getAllProductsJwtAdmin(@Context SecurityIdentity identity) {
    if (!identity.getRoles().contains("admin")) {
      throw new ForbiddenException("No tienes permisos");
    }
    System.out.println("|| JWT admin route accessed || Roles: " + identity.getRoles());
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }

  @GET
  @Path("/jwt/user/products")
  public List<ProductDTO> getAllProductsJwtUser(@Context SecurityIdentity identity) {
    if (!identity.getRoles().contains("user")) {
      throw new ForbiddenException("No tienes permisos");
    }
    System.out.println("|| JWT user route accessed || Roles: " + identity.getRoles());
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }

  // ================= KEYCLOAK =================
  @GET
  @Path("/keycloak/admin/products")
  @RolesAllowed("admin")
  public List<ProductDTO> getAllProductsKeycloakAdmin() {
    System.out.println("|| Keycloak admin route accessed ||");
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }

  @GET
  @Path("/keycloak/user/products")
  @RolesAllowed("user")
  public List<ProductDTO> getAllProductsKeycloakUser() {
    System.out.println("|| Keycloak user route accessed ||");
    List<Product> products = productService.listAll(0, 0, null, null, null, null, true);
    return productMapper.toDTOList(products);
  }
}
