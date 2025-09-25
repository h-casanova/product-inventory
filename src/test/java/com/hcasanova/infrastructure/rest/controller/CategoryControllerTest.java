package com.hcasanova.infrastructure.rest.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerTest {

  // ----------------- BASIC TESTS -----------------

  @Test
  @Order(1)
  void testGetAllCategories() {
    var response = RestAssured.given().when().get("/categories");
    response
        .then()
        .statusCode(200)
        .body("$.size()", greaterThanOrEqualTo(4))
        .body("name", hasItems("Computers", "Peripherals", "Accessories", "Audio/Video"));
  }

  @Test
  @Order(2)
  void testGetCategoryByIdFound() {
    var response = RestAssured.given().when().get("/categories/0");
    response.then().statusCode(200).body("id", is(0)).body("name", is("Computers"));
  }

  @Test
  @Order(3)
  void testGetCategoryByIdNotFound() {
    var response = RestAssured.given().when().get("/categories/999");
    response.then().statusCode(404);
  }

  // ----------------- CREATION TESTS -----------------

  @Test
  @Order(4)
  void testCreateCategorySuccess() {
    String json =
        """
			{
			  "name": "New Category"
			}
			""";

    var createResponse =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/categories");
    int id =
        createResponse.then().statusCode(200).body("name", is("New Category")).extract().path("id");

    var getResponse = RestAssured.given().when().get("/categories/" + id);
    getResponse.then().statusCode(200).body("id", is(id)).body("name", is("New Category"));
  }

  @Test
  @Order(5)
  void testCreateCategoryInvalidData() {
    String json =
        """
			{
			  "name": ""
			}
			""";

    var response =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/categories");
    response.then().statusCode(400);
  }

  // ----------------- UPDATING TESTS -----------------

  @Test
  @Order(6)
  void testUpdateCategorySuccess() {
    String json =
        """
			{
			  "name": "Updated Category"
			}
			""";

    var response =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .put("/categories/0");
    response.then().statusCode(200).body("name", is("Updated Category"));
  }

  @Test
  @Order(7)
  void testUpdateCategoryNotFound() {
    String json =
        """
			{
			  "name": "Nonexistent"
			}
			""";

    var response =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .put("/categories/999");
    response.then().statusCode(404);
  }

  // ----------------- DELETING TESTS -----------------

  @Test
  @Order(8)
  void testDeleteCategorySuccess() {
    String json =
        """
			{
			  "name": "ToDelete"
			}
			""";

    var createResponse =
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body(json)
            .when()
            .post("/categories");
    int id = createResponse.then().statusCode(200).extract().path("id");

    var deleteResponse = RestAssured.given().when().delete("/categories/" + id);
    deleteResponse.then().statusCode(204);

    var getResponse = RestAssured.given().when().get("/categories/" + id);
    getResponse.then().statusCode(404);
  }

  @Test
  @Order(9)
  void testDeleteCategoryNotFound() {
    var response = RestAssured.given().when().delete("/categories/99999");
    response.then().statusCode(404);
  }

  // ----------------- ADD/REMOVE PRODUCT TESTS -----------------

  @Test
  @Order(10)
  void testAddProductToCategory() {
    var response = RestAssured.given().when().post("/categories/0/add-product/0");
    response.then().statusCode(200).body("products.id", hasItem(0));
  }

  @Test
  @Order(11)
  void testAddProductToCategoryNotFound() {
    var response = RestAssured.given().when().post("/categories/999/add-product/0");
    response.then().statusCode(404);
  }

  @Test
  @Order(12)
  void testRemoveProductFromCategory() {
    var response = RestAssured.given().when().post("/categories/0/remove-product/0");
    response.then().statusCode(200).body("products.id", not(hasItem(0)));
  }

  @Test
  @Order(13)
  void testRemoveProductFromCategoryNotFound() {
    var response = RestAssured.given().when().post("/categories/999/remove-product/0");
    response.then().statusCode(404);
  }
}
