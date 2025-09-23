package com.hcasanova.product_inventory;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title="Product Inventory API",
        version = "1.0.0",
        contact = @Contact(
            name = "Humberto CL",
            url = "http://github.com/h-casanova",
            email = "hcasanovalebrero@hotmail.com"),
        license = @License(
            name = "MIT",
            url = "https://mit-license.org/"))
)
public class ProductInventory extends Application {
}