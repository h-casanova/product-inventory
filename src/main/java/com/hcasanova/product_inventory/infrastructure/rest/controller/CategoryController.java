package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.CategoryService;
import com.hcasanova.product_inventory.domain.model.Category;
import com.hcasanova.product_inventory.infrastructure.mapper.CategoryMapper;
import com.hcasanova.product_inventory.infrastructure.rest.dto.CategoryDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

  @Inject CategoryService categoryService;
  @Inject CategoryMapper categoryMapper;

  @GET
  @Operation(
      summary = "Get all categories",
      description = "Returns all categories with their product IDs.")
  public List<CategoryDTO> getAll() {
    return categoryMapper.toDTOList(categoryService.listAll());
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Get category by ID", description = "Returns the category for the given ID.")
  public CategoryDTO getById(@PathParam("id") Long id) {
    Category category = categoryService.findById(id);
    return categoryMapper.toDTO(category);
  }

  @POST
  @Operation(
      summary = "Create a new category",
      description = "Creates a new category with the given name.")
  public CategoryDTO createCategory(@Valid CategoryDTO dto) {
    Category category = categoryService.createCategory(categoryMapper.toEntity(dto));
    return categoryMapper.toDTO(category);
  }

  @PUT
  @Path("/{id}")
  @Operation(
      summary = "Update a category",
      description = "Updates the name of an existing category.")
  public CategoryDTO updateCategory(@PathParam("id") Long id, @Valid CategoryDTO dto) {
    Category category = categoryService.updateCategory(id, dto);
    return categoryMapper.toDTO(category);
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete a category", description = "Deletes the category for the given ID.")
  public void deleteCategory(@PathParam("id") Long id) {
    boolean deleted = categoryService.deleteCategory(id);
    if (!deleted) throw new NotFoundException("Category not found");
  }

  @POST
  @Consumes(MediaType.WILDCARD)
  @Path("/{categoryId}/add-product/{productId}")
  @Operation(
      summary = "Add product to category",
      description = "Adds an existing product to the specified category.")
  public CategoryDTO addProduct(
      @PathParam("categoryId") Long categoryId, @PathParam("productId") Long productId) {
    Category category = categoryService.addProduct(categoryId, productId);
    return categoryMapper.toDTO(category);
  }

  @POST
  @Consumes(MediaType.WILDCARD)
  @Path("/{categoryId}/remove-product/{productId}")
  @Operation(
      summary = "Remove product from category",
      description = "Removes a product from the specified category.")
  public CategoryDTO removeProduct(
      @PathParam("categoryId") Long categoryId, @PathParam("productId") Long productId) {
    Category category = categoryService.removeProduct(categoryId, productId);
    return categoryMapper.toDTO(category);
  }
}
