package com.mikhailkarpov.flights.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FlightNotFoundException extends ResponseStatusException {

    public FlightNotFoundException(String flightCode) {
        super(HttpStatus.NOT_FOUND, "Flight not found by code " + flightCode);
    }
}
