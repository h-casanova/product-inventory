package com.hcasanova.infrastructure.rest.controller;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@QuarkusIntegrationTest
class ProductControllerTest {

    @Test
    void testGetAllProducts() {
        RestAssured.given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("$.size()", is(3))
            .body("name", hasItems("Laptop", "Mouse", "Keyboard"));
    }

    @Test
    void testGetProductByIdFound() {
        RestAssured.given()
            .when().get("/products/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Laptop"))
            .body("price", is(1500.0f)); // Interpretta doubles
    }

    @Test
    void testGetProductByIdNotFound() {
        RestAssured.given()
            .when().get("/products/999")
            .then()
            .statusCode(404);
    }
}
