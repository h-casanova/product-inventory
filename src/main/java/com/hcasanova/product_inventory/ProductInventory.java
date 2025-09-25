package com.hcasanova.product_inventory;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Product Inventory API",
            version = "1.0.0",
            contact =
                @Contact(
                    name = "Humberto CL",
                    url = "http://github.com/h-casanova",
                    email = "hcasanovalebrero@hotmail.com"),
            license = @License(name = "MIT", url = "https://mit-license.org/")),
    security = @SecurityRequirement(name = "jwt"),
    components =
        @Components(
            securitySchemes = {
              @SecurityScheme(
                  securitySchemeName = "jwt",
                  description = "Token JWT",
                  type = SecuritySchemeType.HTTP,
                  scheme = "bearer",
                  bearerFormat = "jwt")
            }))
public class ProductInventory extends Application {}
