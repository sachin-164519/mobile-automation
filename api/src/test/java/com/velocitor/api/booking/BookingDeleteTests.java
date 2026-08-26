package com.velocitor.api.booking;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.support.AuthSupport;
import com.velocitor.api.support.BookingFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@DisplayName("DELETE /booking/{id}")
class BookingDeleteTests extends BaseApiTest {

    /**
     * Known Restful Booker quirk: a successful DELETE responds 201 Created,
     * not 200 or 204 as REST conventions would suggest. Asserted explicitly
     * here (with a comment) so a future reader doesn't "fix" it as a typo.
     */
    @Test
    @DisplayName("DELETE with a valid token succeeds (201) and the booking is gone afterward")
    void deleteWithValidTokenRemovesBooking() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());
        String token = AuthSupport.createValidToken(requestSpec);

        given()
                .spec(requestSpec)
                .cookie("token", token)
                .when()
                .delete("/booking/{id}", bookingId)
                .then()
                .statusCode(201);

        given()
                .spec(requestSpec)
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE without a token is rejected with 403 and the booking survives")
    void deleteWithoutTokenReturns403AndBookingSurvives() {
        int bookingId = createBooking(BookingFactory.uniqueBooking());

        given()
                .spec(requestSpec)
                .when()
                .delete("/booking/{id}", bookingId)
                .then()
                .statusCode(403);

        given()
                .spec(requestSpec)
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(200);
    }

    /**
     * Known Restful Booker quirk: deleting an id that doesn't exist returns
     * 405 Method Not Allowed rather than 404 Not Found. This looks like a bug
     * in the target API (the verb is clearly allowed on this route - it just
     * did), but the suite documents observed behavior rather than the
     * "should be" behavior, and flags the mismatch here instead of silently
     * asserting something that isn't true.
     */
    @Test
    @DisplayName("DELETE on a non-existent id returns 405 (documented API quirk, not 404)")
    void deleteNonExistentBookingReturns405() {
        String token = AuthSupport.createValidToken(requestSpec);
        int implausibleId = Integer.MAX_VALUE;

        given()
                .spec(requestSpec)
                .cookie("token", token)
                .when()
                .delete("/booking/{id}", implausibleId)
                .then()
                .statusCode(405);
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
