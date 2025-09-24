package com.hcasanova.infrastructure.rest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.Matchers.*;

import java.util.List;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {
	
    @Test
    @Order(1)
    void testGetAllProductsInsertedFromFeed() {
        RestAssured.given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("$.size()", is(25))
            .body("name", hasItems("Laptop", "Tablet", "Laptop Bag"));
    }
    
    @Test
    @Order(2)
    void testPagination() {
        RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("$.size()", is(5));
    }
    
    @Test
    @Order(3)
    void testPaginationInvalidParams() {
        RestAssured.given()
            .queryParam("page", -1)
            .queryParam("size", -5)
            .when().get("/products")
            .then()
            .statusCode(500);
    }


    @Test
    @Order(4)
    void testGetProductByIdFound() {
        RestAssured.given()
            .when().get("/products/0")
            .then()
            .statusCode(200)
            .body("id", is(0))
            .body("name", is("Laptop"))
            .body("price", is(1500.0f)); // Interpretaa doubles
    }

    @Test
    @Order(5)
    void testGetProductByIdNotFound() {
        RestAssured.given()
            .when().get("/products/999")
            .then()
            .statusCode(404);
    }
    
    @Test
    @Order(6)
    void testCreateSingleProduct() {
        String json = """
            {
                "name": "Laptop 2025",
                "description": "Cutting edge tech",
                "price": 100,
                "quantity": 15
            }
        """;

        int id = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when().post("/products/create-single")
            .then()
            .statusCode(200)
            .body("name", is("Laptop 2025"))
            .body("price", is(100.0f))
            .body("quantity", is(15))
            .extract()
            .path("id");

        RestAssured.given()
            .when().get("/products/" + id)
            .then()
            .statusCode(200)
            .body("id", is(id))
            .body("name", is("Laptop 2025"));
    }
    
    @Test
    @Order(7)
    void testCreateBulkProducts() {
        String json = """
            [
                {
                    "name": "Monitor 2025",
                    "description": "Cutting edge tech",
                    "price": 2500,
                    "quantity": 10
                },
                {
                    "name": "Mouse 2025",
                    "description": "Cutting edge tech",
                    "price": 100,
                    "quantity": 15
                }
            ]
        """;

        List<Integer> ids = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when().post("/products/create-bulk")
            .then()
            .statusCode(200)
            .body("$.size()", is(2))
            .body("name", hasItems("Monitor 2025", "Mouse 2025"))
            .extract()
            .path("id");

        for (Integer id : ids) {
            RestAssured.given()
                .when().get("/products/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id));
        }
    }
    
    @Test
    @Order(8)
    void testCreateSingleProductInvalidData() {
        String json = """
            {
                "name": "",
                "description": "Invalid product",
                "price": -10,
                "quantity": -5
            }
        """;

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when().post("/products/create-single")
            .then()
            .statusCode(400);
    }
   
    @Test
    @Order(9)
    void testCreateBulkProductsEmptyList() {
    	String json = """
                {
                    "name": "Laptop 2025",
                    "description": "Cutting edge tech",
                    "price": 100,
                    "quantity": 15
                }
            """;

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when().post("/products/create-bulk")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(10)
    void testUpdateExistingProductSuccess() {
        String updateJson = """
            {
                "name": "Laptop UPDATED",
                "description": "Updated description",
                "price": 1600,
                "quantity": 7,
                "version": 0
            }
        """;

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(updateJson)
            .when().put("/products/0")
            .then()
            .statusCode(200)
            .body("name", is("Laptop UPDATED"))
            .body("price", is(1600.0f))
            .body("quantity", is(7))
            .body("version", is(1));
    }

    @Test
    @Order(11)
    void testUpdateExistingProductVersionMismatch() {
        String updateJson = """
            {
                "name": "Laptop FAILED",
                "description": "This should fail",
                "price": 1700,
                "quantity": 8,
                "version": 0
            }
        """;

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(updateJson)
            .when().put("/products/0")
            .then()
            .statusCode(500)
            .body(containsString("Version mismatch"));
    }

    @Test
    @Order(12)
    void testUpdateNonExistingProduct() {
        String updateJson = """
            {
                "name": "Nonexistent",
                "description": "Does not exist",
                "price": 50,
                "quantity": 5,
                "version": 0
            }
        """;

        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(updateJson)
            .when().put("/products/999")
            .then()
            .statusCode(404);
    }
    
    @Test
    @Order(13)
    void testDeleteExistingProduct() {
        String json = """
            {
                "name": "ToDelete",
                "description": "Product to be deleted",
                "price": 50,
                "quantity": 5
            }
        """;

        int id = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when().post("/products/create-single")
            .then()
            .statusCode(200)
            .extract()
            .path("id");

        RestAssured.given()
            .when().delete("/products/" + id)
            .then()
            .statusCode(200)
            .body("message", is("Product deleted successfully"));

        RestAssured.given()
            .when().get("/products/" + id)
            .then()
            .statusCode(404);
    }

    @Test
    @Order(14)
    void testDeleteNonExistingProduct() {
        RestAssured.given()
            .when().delete("/products/99999")
            .then()
            .statusCode(404);
    }

}
