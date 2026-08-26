package com.velocitor.api.base;

import com.velocitor.api.config.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Shared base for all API test classes.
 *
 * <p>Only wiring lives here (base URI, content type) — no fixture data and no
 * mutable instance state, so subclasses stay free to run their {@code @Test}
 * methods in any order, in isolation, or in parallel without interfering with
 * each other. Anything a specific test needs (a token, a booking) is created
 * fresh by that test.
 */
public abstract class BaseApiTest {

    protected static RequestSpecification requestSpec;

    @BeforeAll
    static void setUpRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(TestConfig.baseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
