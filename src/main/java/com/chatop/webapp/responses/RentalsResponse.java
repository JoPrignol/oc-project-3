package com.chatop.webapp.responses;

public class RentalsResponse {

    private Iterable<RentalResponse> rentals;

    public RentalsResponse(Iterable<RentalResponse> rentals) {
        this.rentals = rentals;
    }

    public Iterable<RentalResponse> getRentals() {
        return rentals;
    }

    public void setRentals(Iterable<RentalResponse> rentals) {
        this.rentals = rentals;
    }
}
