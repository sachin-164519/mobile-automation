package com.velocitor.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Maps to the booking JSON object used across the Restful Booker API
 * (create/get/update request and response bodies).
 *
 * <p>A builder is provided so tests can express intent clearly
 * ({@code Booking.builder().firstname("Jim")...build()}) while Jackson still
 * gets the no-arg constructor + setters it wants for deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    private String firstname;
    private String lastname;
    private Integer totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    /** Required by Jackson for deserialization. */
    public Booking() {
    }

    private Booking(Builder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.totalprice = builder.totalprice;
        this.depositpaid = builder.depositpaid;
        this.bookingdates = builder.bookingdates;
        this.additionalneeds = builder.additionalneeds;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Returns a builder pre-populated with this booking's current values, for partial edits. */
    public Builder toBuilder() {
        return new Builder()
                .firstname(firstname)
                .lastname(lastname)
                .totalprice(totalprice)
                .depositpaid(depositpaid)
                .bookingdates(bookingdates)
                .additionalneeds(additionalneeds);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Integer totalprice) {
        this.totalprice = totalprice;
    }

    public Boolean getDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(Boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public BookingDates getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        Booking booking = (Booking) o;
        return Objects.equals(firstname, booking.firstname)
                && Objects.equals(lastname, booking.lastname)
                && Objects.equals(totalprice, booking.totalprice)
                && Objects.equals(depositpaid, booking.depositpaid)
                && Objects.equals(bookingdates, booking.bookingdates)
                && Objects.equals(additionalneeds, booking.additionalneeds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
    }

    @Override
    public String toString() {
        return "Booking{"
                + "firstname='" + firstname + '\''
                + ", lastname='" + lastname + '\''
                + ", totalprice=" + totalprice
                + ", depositpaid=" + depositpaid
                + ", bookingdates=" + bookingdates
                + ", additionalneeds='" + additionalneeds + '\''
                + '}';
    }

    public static final class Builder {
        private String firstname;
        private String lastname;
        private Integer totalprice;
        private Boolean depositpaid;
        private BookingDates bookingdates;
        private String additionalneeds;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder totalprice(Integer totalprice) {
            this.totalprice = totalprice;
            return this;
        }

        public Builder depositpaid(Boolean depositpaid) {
            this.depositpaid = depositpaid;
            return this;
        }

        public Builder bookingdates(BookingDates bookingdates) {
            this.bookingdates = bookingdates;
            return this;
        }

        public Builder additionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
