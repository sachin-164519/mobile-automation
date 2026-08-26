package com.velocitor.api.support;

import com.velocitor.api.model.Booking;
import com.velocitor.api.model.BookingDates;

import java.util.UUID;

/**
 * Builds valid {@link Booking} test data with randomized names.
 *
 * <p>Randomizing names (rather than reusing fixed values like "Jim Brown")
 * matters here specifically because the target API resets its dataset every
 * ~10 minutes and is used concurrently by anyone running these tests. Unique
 * data per test run avoids flaky collisions with pre-seeded or other
 * test-runs' records when filtering/searching by name.
 */
public final class BookingFactory {

    private BookingFactory() {
    }

    /** A syntactically valid, uniquely-named booking suitable as a baseline test fixture. */
    public static Booking uniqueBooking() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        return Booking.builder()
                .firstname("Test" + suffix)
                .lastname("User" + suffix)
                .totalprice(150)
                .depositpaid(true)
                .bookingdates(new BookingDates("2025-01-01", "2025-01-10"))
                .additionalneeds("Breakfast")
                .build();
    }
}
