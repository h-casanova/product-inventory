package com.hcasanova.product_inventory.infrastructure.mapper;

import com.hcasanova.product_inventory.domain.model.Product;
import com.hcasanova.product_inventory.infrastructure.rest.dto.ProductDTO;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductMapper {

    // Mapper to not expose the model layer
	
	public Product toEntity(ProductDTO request) {
	    Product product = new Product();
	    product.name = request.name;
	    product.description = request.description;
	    product.price = request.price;
	    product.quantity = request.quantity;
	    product.version = request.version;
	    return product;
	}


	public ProductDTO toDTO(Product product) {
	    ProductDTO dto = new ProductDTO();
	    dto.name = product.name;
	    dto.description = product.description;
	    dto.price = product.price;
	    dto.quantity = product.quantity;
	    dto.version = product.version;
	    dto.id = product.id;
	    return dto;
	}

    public List<ProductDTO> toDTOList(List<Product> products) {
        return products.stream()
                       .map(this::toDTO)
                       .collect(Collectors.toList());
    }
}
