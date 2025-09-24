package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.ProductService;
import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.mapper.ProductMapper;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ApiResponseDTO;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService productService;

    @Inject
    ProductMapper productMapper;

    @GET
    @Operation(summary = "Get all products", description = "Returns all products. Supports optional pagination, filtering and sorting.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "List of products returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "400", description = "Invalid parameters")
    })
    public List<ProductDTO> getAll(
            @QueryParam("page") @Min(value = 0, message = "Page must be >= 0") @DefaultValue("0") int page,
            @QueryParam("size") @Min(value = 0, message = "Size must be >= 0") @DefaultValue("0") int size,
            @QueryParam("name") String name,
            @QueryParam("minPrice") Double minPrice,
            @QueryParam("maxPrice") Double maxPrice,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortAsc") @DefaultValue("true") boolean sortAsc
    ) {
        List<Product> products = productService.listAll(page, size, name, minPrice, maxPrice, sortBy, sortAsc);
        return productMapper.toDTOList(products);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns the product for the given ID.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "404", description = "Product not found")
    })
    public ProductDTO getById(@PathParam("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) throw new NotFoundException("Product not found");
        return productMapper.toDTO(product);
    }

    @POST
    @Path("/create-single")
    @Operation(summary = "Create a single product", description = "Creates a new product from given data.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "400", description = "Invalid product data")
    })
    public ProductDTO createSingleProduct(@Valid ProductDTO request) {
        return productMapper.toDTO(productService.createSingleProduct(productMapper.toEntity(request)));
    }

    @POST
    @Path("/create-bulk")
    @Operation(summary = "Create multiple products", description = "Creates multiple products from the provided list.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Products created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "400", description = "Invalid input or empty list")
    })
    public List<ProductDTO> createBulkProducts(List<@Valid ProductDTO> requests) {
        return productMapper.toDTOList(productService.createBulkProducts(
            requests.stream().map(productMapper::toEntity).toList()));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product. Version must be provided.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
        @APIResponse(responseCode = "404", description = "Product not found"),
        @APIResponse(responseCode = "500", description = "Version mismatch")
    })
    public ProductDTO updateProduct(@PathParam("id") Long id, @Valid ProductDTO request) {
        if (request.version == null) throw new BadRequestException("Version is required for update");
        return productMapper.toDTO(productService.updateProduct(id, request));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes the product for the given ID.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))),
        @APIResponse(responseCode = "404", description = "Product not found")
    })
    public ApiResponseDTO deleteProduct(@PathParam("id") Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) throw new NotFoundException("Product not found");
        return new ApiResponseDTO("Product deleted successfully");
    }
}