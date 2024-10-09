package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class NumberResourceTest {

    @Test
    public void testNumberEndpoint() {
        given()
          .when().get("/numbers")
          .then()
             .statusCode(200)
             .body(containsString("Sent: "));
    }
}