package com.hcasanova.infrastructure.rest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductControllerTest {

    @Test
    void testGetAllProducts() {
        RestAssured.given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("$.size()", is(25))
            .body("name", hasItems("Laptop", "Smartphone", "Laptop Bag"));
    }
    
    @Test
    void testPagination() {
        RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("$.size()", is(5))
            .body("id", hasItems(1, 2, 3, 4, 5))
            .body("id", not(hasItem(6)));
    }

    @Test
    void testGetProductByIdFound() {
        RestAssured.given()
            .when().get("/products/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Laptop"))
            .body("price", is(1500.0f)); // Interpretaa doubles
    }

    @Test
    void testGetProductByIdNotFound() {
        RestAssured.given()
            .when().get("/products/999")
            .then()
            .statusCode(404);
    }
}
