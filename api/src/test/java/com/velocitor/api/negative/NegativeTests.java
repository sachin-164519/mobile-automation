package com.velocitor.api.negative;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.model.BookingDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

/**
 * Adversarial / edge-case coverage that goes beyond the happy path.
 *
 * <p>A few of these assert on <em>observed, documented</em> Restful Booker
 * behavior rather than "textbook REST" behavior - this API is known to do
 * very little server-side validation. Where that's the case, the test names
 * and comments say so explicitly, so a future reader can tell "this is
 * pinning a known API bug" apart from "this is what I expect correct
 * behavior to be."
 */
@DisplayName("Adversarial / edge-case input handling")
class NegativeTests extends BaseApiTest {

    @Test
    @DisplayName("firstname accepts a numeric string with no type validation (documented API gap)")
    void numericStringAcceptedAsFirstname() {
        Booking booking = Booking.builder()
                .firstname("1234567")
                .lastname("Test")
                .totalprice(200)
                .depositpaid(true)
                .bookingdates(new BookingDates("2026-07-01", "2026-07-05"))
                .build();

        given()
                .spec(requestSpec)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("1234567"));
    }

    @Test
    @DisplayName("a non-ISO checkin/checkout date is silently accepted and corrupted, not rejected")
    void malformedDateIsSilentlyCorruptedNotRejected() {
        String malformedRequestBody = """
                {
                  "firstname": "Date",
                  "lastname": "Corruption",
                  "totalprice": 100,
                  "depositpaid": true,
                  "bookingdates": {
                    "checkin": "01/01/2026",
                    "checkout": "05/01/2026"
                  }
                }
                """;

        // Documents a genuine data-integrity bug: instead of a 400, the API accepts a
        // non-ISO date and silently stores a corrupted value rather than the date sent.
        // A test that only checked "was it a 2xx?" would miss this - the point here is
        // the response body, not the status code.
        given()
                .spec(requestSpec)
                .body(malformedRequestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.bookingdates.checkin", containsString("NaN"));
    }

    @Test
    @DisplayName("negative totalprice is accepted without server-side range validation")
    void negativeTotalPriceIsAccepted() {
        Booking booking = Booking.builder()
                .firstname("Negative")
                .lastname("Price")
                .totalprice(-500)
                .depositpaid(true)
                .bookingdates(new BookingDates("2026-01-01", "2026-01-05"))
                .build();

        given()
                .spec(requestSpec)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                // Not asserting this SHOULD be 200 - asserting it currently IS, so that if
                // this is ever fixed to reject negative prices, the change shows up as a
                // meaningful, visible test failure rather than going unnoticed.
                .statusCode(200)
                .body("booking.totalprice", equalTo(-500));
    }

    @Test
    @DisplayName("a script-injection style payload in firstname is stored as inert text, not executed")
    void scriptInjectionPayloadIsStoredAsPlainText() {
        String payload = "<script>alert(1)</script>";
        Booking booking = Booking.builder()
                .firstname(payload)
                .lastname("Injection")
                .totalprice(100)
                .depositpaid(true)
                .bookingdates(new BookingDates("2026-01-01", "2026-01-05"))
                .build();

        given()
                .spec(requestSpec)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo(payload));
    }

    @Test
    @DisplayName("a booking missing the required bookingdates object does not crash the server")
    void missingBookingDatesObjectDoesNotCauseServerError() {
        String bodyMissingDates = """
                {
                  "firstname": "No",
                  "lastname": "Dates",
                  "totalprice": 100,
                  "depositpaid": true
                }
                """;

        given()
                .spec(requestSpec)
                .body(bodyMissingDates)
                .when()
                .post("/booking")
                .then()
                // Bounding the outcome rather than asserting one exact code: either a
                // client error (ideal) or a 2xx that accepts incomplete data (consistent
                // with this API's generally lax validation elsewhere) are both
                // informative. A 5xx is the one result that would be an unambiguous
                // regression worth failing the build over.
                .statusCode(lessThan(500));
    }

    @Test
    @DisplayName("malformed JSON syntax in the request body is rejected, not treated as success")
    void malformedJsonSyntaxIsRejected() {
        String brokenJson = "{ \"firstname\": \"Broken\", \"lastname\": ";

        given()
                .spec(requestSpec)
                .body(brokenJson)
                .when()
                .post("/booking")
                .then()
                .statusCode(not(200));
    }
}
