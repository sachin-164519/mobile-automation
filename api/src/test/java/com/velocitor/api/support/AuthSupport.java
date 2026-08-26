package com.velocitor.api.support;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Auth token helper for tests that need to hit authenticated endpoints
 * (PUT / PATCH / DELETE on a booking).
 *
 * <p>Deliberately <b>not</b> cached as shared static state: each test that
 * needs a token calls {@link #createValidToken(RequestSpecification)} itself.
 * That costs one extra HTTP call per test, but it means no test's success
 * depends on setup performed by another test or by execution order — which is
 * exactly the independence the assignment asks for.
 */
public final class AuthSupport {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password123";

    private AuthSupport() {
    }

    /** Creates a token with valid credentials and returns it. Fails the test via assertion if auth is down. */
    public static String createValidToken(RequestSpecification spec) {
        String token = given()
                .spec(spec)
                .body("""
                        {
                          "username": "%s",
                          "password": "%s"
                        }
                        """.formatted(VALID_USERNAME, VALID_PASSWORD))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", org.hamcrest.Matchers.notNullValue())
                .extract()
                .path("token");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Auth succeeded but returned no usable token");
        }
        return token;
    }
}
