package com.hcasanova.infrastructure.rest.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

  // ----------------- BASIC TESTS -----------------

  @Test
  @Order(1)
  void testGetAllProductsInsertedFromFeed() {
    RestAssured.given()
        .when()
        .get("/products")
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
        .when()
        .get("/products")
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
        .when()
        .get("/products")
        .then()
        .statusCode(400);
  }

  // ----------------- FILTERS TESTS -----------------

  @Test
  @Order(4)
  void testFilterByName() {
    RestAssured.given()
        .queryParam("name", "Laptop")
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("$.size()", greaterThanOrEqualTo(2))
        .body("name", everyItem(containsString("Laptop")));
  }

  @Test
  @Order(5)
  void testFilterByPriceRange() {
    RestAssured.given()
        .queryParam("minPrice", 100.0)
        .queryParam("maxPrice", 500.0)
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("price", everyItem(allOf(greaterThanOrEqualTo(100.0f), lessThanOrEqualTo(500.0f))));
  }

  // ----------------- ORDERING TESTS -----------------

  @Test
  @Order(6)
  void testSortByNameAsc() {
    List<String> names =
        RestAssured.given()
            .queryParam("sortBy", "name")
            .queryParam("sortAsc", true)
            .queryParam("size", 25)
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("name", String.class);

    for (int i = 1; i < names.size(); i++) {
      assertThat("Names not sorted ascending", names.get(i - 1).compareTo(names.get(i)) <= 0);
    }
  }

  @Test
  @Order(7)
  void testSortByPriceDesc() {
    List<Float> prices =
        RestAssured.given()
            .queryParam("sortBy", "price")
            .queryParam("sortAsc", false)
            .queryParam("size", 25)
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("price", Float.class);

    for (int i = 1; i < prices.size(); i++) {
      assertThat("Prices not sorted descending", prices.get(i - 1) >= prices.get(i));
    }
  }

  // ----------------- GET BY ID TESTS -----------------

  @Test
  @Order(8)
  void testGetProductByIdFound() {
    RestAssured.given()
        .when()
        .get("/products/0")
        .then()
        .statusCode(200)
        .body("id", is(0))
        .body("name", is("Laptop"))
        .body("price", is(1500.0f));
  }

  @Test
  @Order(9)
  void testGetProductByIdNotFound() {
    RestAssured.given().when().get("/products/999").then().statusCode(404);
  }

  // ----------------- CREATION TESTS -----------------

  @Test
  @Order(10)
  void testCreateSingleProduct() {
    String json =
        """
			    {
			        "name": "Laptop 2025",
			        "description": "Cutting edge tech",
			        "price": 100,
			        "quantity": 15
			    }
			""";

    int id =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/products/create-single")
            .then()
            .statusCode(200)
            .body("name", is("Laptop 2025"))
            .body("price", is(100.0f))
            .body("quantity", is(15))
            .extract()
            .path("id");

    RestAssured.given()
        .when()
        .get("/products/" + id)
        .then()
        .statusCode(200)
        .body("id", is(id))
        .body("name", is("Laptop 2025"));
  }

  @Test
  @Order(11)
  void testCreateBulkProducts() {
    String json =
        """
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

    List<Integer> ids =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/products/create-bulk")
            .then()
            .statusCode(200)
            .body("$.size()", is(2))
            .body("name", hasItems("Monitor 2025", "Mouse 2025"))
            .extract()
            .path("id");

    for (Integer id : ids) {
      RestAssured.given().when().get("/products/" + id).then().statusCode(200).body("id", is(id));
    }
  }

  @Test
  @Order(12)
  void testCreateSingleProductInvalidData() {
    String json =
        """
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
        .when()
        .post("/products/create-single")
        .then()
        .statusCode(400);
  }

  @Test
  @Order(13)
  void testCreateBulkProductsEmptyList() {
    String json =
        """
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
        .when()
        .post("/products/create-bulk")
        .then()
        .statusCode(400);
  }

  // ----------------- UPDATING TESTS -----------------

  @Test
  @Order(14)
  void testUpdateExistingProductSuccess() {
    int currentVersion =
        RestAssured.given()
            .when()
            .get("/products/0")
            .then()
            .statusCode(200)
            .extract()
            .path("version");

    String updateJson =
        """
			{
			  "name": "Laptop UPDATED",
			  "description": "Updated description",
			  "price": 1600,
			  "quantity": 7,
			  "version": %d
			}
			"""
            .formatted(currentVersion);

    RestAssured.given()
        .header("Content-Type", "application/json")
        .body(updateJson)
        .when()
        .put("/products/0")
        .then()
        .statusCode(200)
        .body("version", is(currentVersion + 1));
  }

  @Test
  @Order(15)
  void testUpdateExistingProductVersionMismatch() {
    String updateJson =
        """
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
        .when()
        .put("/products/0")
        .then()
        .statusCode(500)
        .body(containsString("Version mismatch"));
  }

  @Test
  @Order(16)
  void testUpdateNonExistingProduct() {
    String updateJson =
        """
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
        .when()
        .put("/products/999")
        .then()
        .statusCode(404);
  }

  // ----------------- DELETING TESTS -----------------

  @Test
  @Order(17)
  void testDeleteExistingProduct() {
    String json =
        """
			    {
			        "name": "ToDelete",
			        "description": "Product to be deleted",
			        "price": 50,
			        "quantity": 5
			    }
			""";

    int id =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/products/create-single")
            .then()
            .statusCode(200)
            .extract()
            .path("id");

    RestAssured.given().when().delete("/products/" + id).then().statusCode(204);

    RestAssured.given().when().get("/products/" + id).then().statusCode(404);
  }

  @Test
  @Order(18)
  void testDeleteNonExistingProduct() {
    RestAssured.given().when().delete("/products/99999").then().statusCode(404);
  }
}
