package com.velocitor.api.booking;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.model.BookingDates;
import com.velocitor.api.support.AuthSupport;
import com.velocitor.api.support.BookingFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PUT /booking/{id}, PATCH /booking/{id}")
class BookingUpdateTests extends BaseApiTest {

    @Test
    @DisplayName("PUT with a valid token fully replaces the booking and persists")
    void putWithValidTokenReplacesBooking() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());
        String token = AuthSupport.createValidToken(requestSpec);

        Booking replacement = Booking.builder()
                .firstname("Updated")
                .lastname("Person")
                .totalprice(999)
                .depositpaid(false)
                .bookingdates(new BookingDates("2026-06-01", "2026-06-10"))
                .additionalneeds("Late checkout")
                .build();

        Booking returned = given()
                .spec(requestSpec)
                .cookie("token", token)
                .body(replacement)
                .when()
                .put("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        assertThat(returned).usingRecursiveComparison().isEqualTo(replacement);

        // Confirm the change actually persisted, not just echoed in the response.
        Booking persisted = given()
                .spec(requestSpec)
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        assertThat(persisted).usingRecursiveComparison().isEqualTo(replacement);
    }

    @Test
    @DisplayName("PUT without a token is rejected with 403")
    void putWithoutTokenReturns403() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());

        given()
                .spec(requestSpec)
                // no cookie / no Authorization header
                .body(BookingFactory.uniqueBooking())
                .when()
                .put("/booking/{id}", bookingId)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("PUT with a bogus token is rejected with 403")
    void putWithBogusTokenReturns403() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());

        given()
                .spec(requestSpec)
                .cookie("token", "this-is-not-a-real-token")
                .body(BookingFactory.uniqueBooking())
                .when()
                .put("/booking/{id}", bookingId)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("PATCH with a valid token updates only the submitted fields")
    void patchWithValidTokenUpdatesOnlySubmittedFields() {
        Booking original = BookingFactory.uniqueBooking();
        int bookingId = createBooking(original);
        String token = AuthSupport.createValidToken(requestSpec);

        Booking returned = given()
                .spec(requestSpec)
                .cookie("token", token)
                .body("""
                        {
                          "totalprice": 555
                        }
                        """)
                .when()
                .patch("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        Booking expected = original.toBuilder().totalprice(555).build();

        assertThat(returned)
                .as("only totalprice should differ from the original booking")
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("PATCH without a token is rejected with 403")
    void patchWithoutTokenReturns403() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());

        given()
                .spec(requestSpec)
                .body("""
                        {
                          "totalprice": 1
                        }
                        """)
                .when()
                .patch("/booking/{id}", bookingId)
                .then()
                .statusCode(403);
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
