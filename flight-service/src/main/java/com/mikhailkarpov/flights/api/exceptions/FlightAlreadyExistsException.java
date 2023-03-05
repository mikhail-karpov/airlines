package com.mikhailkarpov.flights.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FlightAlreadyExistsException extends ResponseStatusException {

    public FlightAlreadyExistsException(String flightCode) {
        super(HttpStatus.BAD_REQUEST, "Flight with code " + flightCode + " already exists");
    }
}
