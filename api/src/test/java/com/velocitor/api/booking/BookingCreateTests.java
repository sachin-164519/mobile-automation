package com.velocitor.api.booking;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.model.BookingDates;
import com.velocitor.api.support.BookingFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("POST /booking")
class BookingCreateTests extends BaseApiTest {

    @Test
    @DisplayName("creates a booking and echoes back exactly what was sent")
    void createBookingRoundTripsRequestData() {
        Booking requested = BookingFactory.uniqueBooking();

        Response response = given()
                .spec(requestSpec)
                .body(requested)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("bookingid", greaterThan(0))
                .extract()
                .response();

        int bookingId = response.path("bookingid");
        Booking created = response.jsonPath().getObject("booking", Booking.class);

        // Full-object comparison catches subtler bugs (wrong field mapping, dropped
        // nested dates, type coercion) that field-by-field spot checks can miss.
        assertThat(created)
                .as("booking echoed by POST /booking id=%d should match what was submitted", bookingId)
                .usingRecursiveComparison()
                .isEqualTo(requested);
    }

    @Test
    @DisplayName("created booking is retrievable afterward with the same data")
    void createdBookingIsRetrievableWithMatchingData() {
        Booking requested = BookingFactory.uniqueBooking();

        int bookingId = given()
                .spec(requestSpec)
                .body(requested)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        Booking fetched = given()
                .spec(requestSpec)
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        assertThat(fetched).usingRecursiveComparison().isEqualTo(requested);
    }

    @Test
    @DisplayName("additionalneeds is optional and can be omitted")
    void additionalNeedsIsOptional() {
        Booking requested = Booking.builder()
                .firstname("Optional")
                .lastname("Fields")
                .totalprice(200)
                .depositpaid(false)
                .bookingdates(new BookingDates("2025-03-01", "2025-03-05"))
                // additionalneeds intentionally omitted
                .build();

        given()
                .spec(requestSpec)
                .body(requested)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", notNullValue());
    }
}
