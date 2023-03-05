package com.mikhailkarpov.flights.api;

import java.util.Optional;

public interface FlightService {

    Flight createFlight(CreateFlightRequest request);

    Optional<Flight> findFlightByCode(String code);

}
