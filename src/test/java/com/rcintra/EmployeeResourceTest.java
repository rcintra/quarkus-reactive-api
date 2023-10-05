package com.rcintra;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class EmployeeResourceTest {

    @Test
    public void testGetAll() {
         given()
          .when().get("/employees")
          .then()
          .statusCode(200);
    }

    @Test
    public void testGetEndpoint() {
        given()
          .when().get("/employees")
          .then()
             .statusCode(200)
             .body("$.size()", is(4),
                     "[0].name", is("John"),
                     "[0].email", is("john@rcintra.com"));
    }

    @Test
    public void testGetById() {
        given()
          .when().get("/employees/10001")
          .then()
             .statusCode(200)
             .body("name", is("John"),
                     "email", is("john@rcintra.com"));
    }

    @Test
    public void testGetByIdNotFound() {
        given()
          .when().get("/employees/533399")
          .then()
             .statusCode(404);
    } 

    @Test
    public void testPost() {
        given()
          .when()
            .contentType("application/json")
            .body("{\"name\": \"Mary\", \"email\": \"mary@rcintra.com\"}")
            .post("/employees")
            .then()
                 .statusCode(201)
                 .body("name", is("Mary"),
                         "email", is("mary@rcintra.com"));
    }

    @Test
    public void testFailPostNoName() {
        given()
          .when()
            .contentType("application/json")
            .body("{\"email\": \"rafael@rcintra.com\"}")
            .post("/employees")
            .then()
                 .statusCode(400);
    }

    @Test
    public void testPut() {
        given()
          .when()
            .contentType("application/json")
            .body("{\"name\": \"Mary\", \"email\": \"mary@rcintra.com\"}")
            .put("/employees/20001")
            .then()
                 .statusCode(200)
                 .body("name", is("Mary"),
                         "email", is("mary@rcintra.com"));
    }
}