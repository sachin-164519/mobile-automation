package com.velocitor.api.auth;

import com.velocitor.api.base.BaseApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("POST /auth")
class AuthTests extends BaseApiTest {

    @Test
    @DisplayName("valid credentials return a usable token")
    void validCredentialsReturnToken() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                          "username": "admin",
                          "password": "password123"
                        }
                        """)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString()));
    }

    /**
     * Known Restful Booker quirk: invalid credentials do NOT produce a 4xx
     * response. The API still replies 200 OK, and the only signal that auth
     * failed is a {@code reason: "Bad credentials"} field in the body instead
     * of a token. A test that only checks the status code would pass here
     * even though authentication silently failed - asserting on the body is
     * the point of this test.
     */
    @Test
    @DisplayName("invalid credentials return 200 with a 'Bad credentials' reason, not a token")
    void invalidCredentialsReturnBadCredentialsReason() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                          "username": "admin",
                          "password": "wrong-password"
                        }
                        """)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("$", org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasKey("token")));
    }

    @Test
    @DisplayName("missing password field is treated as bad credentials, not a server error")
    void missingPasswordFieldReturnsBadCredentials() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                          "username": "admin"
                        }
                        """)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"));
    }

    @Test
    @DisplayName("empty request body does not crash the endpoint")
    void emptyBodyIsHandledGracefully() {
        given()
                .spec(requestSpec)
                .body("{}")
                .when()
                .post("/auth")
                .then()
                // Documenting actual behavior rather than assuming: the API is expected to
                // respond with *some* well-formed 2xx/4xx JSON body rather than a 5xx crash.
                .statusCode(org.hamcrest.Matchers.lessThan(500));
    }
}
