package com.velocitor.api.booking;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.support.BookingFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@DisplayName("GET /booking, GET /booking/{id}")
class BookingReadTests extends BaseApiTest {

    @Test
    @DisplayName("GET /booking/{id} returns the booking that was created")
    void getByIdReturnsCreatedBooking() {
        Booking requested = BookingFactory.uniqueBooking();
        int bookingId = createBooking(requested);

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
    @DisplayName("GET /booking/{id} for a non-existent id returns 404")
    void getByIdForMissingBookingReturns404() {
        int implausibleId = Integer.MAX_VALUE;

        given()
                .spec(requestSpec)
                .when()
                .get("/booking/{id}", implausibleId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /booking/{id} with a non-numeric id is rejected, not a 500")
    void getByIdWithNonNumericIdIsHandledGracefully() {
        given()
                .spec(requestSpec)
                .when()
                .get("/booking/not-a-number")
                .then()
                .statusCode(org.hamcrest.Matchers.anyOf(equalTo(404), equalTo(400)));
    }

    @Test
    @DisplayName("GET /booking lists ids and includes a newly created booking")
    void listAllBookingsIncludesNewlyCreated() {
        Booking requested = BookingFactory.uniqueBooking();
        int bookingId = createBooking(requested);

        java.util.List<Integer> ids = given()
                .spec(requestSpec)
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("bookingid", Integer.class);

        assertThat(ids)
                .as("full booking list should contain the id just created")
                .isNotEmpty()
                .contains(bookingId);
    }

    @Test
    @DisplayName("GET /booking returns at least the 10 seed records the API ships with")
    void listAllBookingsHasSeedData() {
        given()
                .spec(requestSpec)
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(10));
    }

    private int createBooking(Booking booking) {
        return given()
                .spec(requestSpec)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");
    }
}
