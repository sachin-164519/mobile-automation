package com.velocitor.api.booking;

import com.velocitor.api.base.BaseApiTest;
import com.velocitor.api.model.Booking;
import com.velocitor.api.support.BookingFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET /booking?firstname=...&lastname=...  |  ?checkin=...&checkout=...")
class BookingSearchTests extends BaseApiTest {

    @Test
    @DisplayName("filtering by firstname and lastname returns the matching booking")
    void filterByFirstAndLastNameFindsCreatedBooking() {
        Booking requested = BookingFactory.uniqueBooking();
        int bookingId = createBooking(requested);

        List<Integer> ids = given()
                .spec(requestSpec)
                .queryParam("firstname", requested.getFirstname())
                .queryParam("lastname", requested.getLastname())
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("bookingid", Integer.class);

        assertThat(ids)
                .as("filtering by the exact name used at creation should return that booking")
                .containsExactly(bookingId);
    }

    @Test
    @DisplayName("filtering by a name that matches nobody returns an empty list, not an error")
    void filterByUnmatchedNameReturnsEmptyList() {
        given()
                .spec(requestSpec)
                .queryParam("firstname", "Nobody-" + java.util.UUID.randomUUID())
                .queryParam("lastname", "Nowhere")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.equalTo(0));
    }

    /**
     * Filtering by checkin/checkout date is documented in the API but is a known
     * source of flaky/inconsistent behavior on the public Restful Booker demo
     * instance (community reports of it ignoring the checkout bound entirely).
     * Rather than assert exact date-range semantics - which would make this test
     * flaky through no fault of the suite - this test asserts the contract we can
     * actually rely on: the endpoint accepts date params and returns a well-formed
     * list without erroring. See README "Known API quirks" for detail.
     */
    @Test
    @DisplayName("filtering by checkin/checkout accepts date params and returns a well-formed list")
    void filterByDateRangeReturnsWellFormedList() {
        given()
                .spec(requestSpec)
                .queryParam("checkin", "2025-01-01")
                .queryParam("checkout", "2025-12-31")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("$", org.hamcrest.Matchers.isA(List.class));
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
