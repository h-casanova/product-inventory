package com.hcasanova.infrastructure.rest.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AuthControllerTest {

  @Test
  public void testLoginSuccess() {
    String requestBody =
        """
			{
			    "username": "admin",
			    "password": "admin123"
			}
			""";

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(200)
        .body("token", notNullValue());
  }

  @Test
  public void testLoginFailure() {
    String requestBody =
        """
			{
			    "username": "admin",
			    "password": "wrongpassword"
			}
			""";

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(401)
        .body("error", equalTo("Invalid credentials"));
  }
}
