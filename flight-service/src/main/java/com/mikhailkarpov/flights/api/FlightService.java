package com.mikhailkarpov.flights.api;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    Flight createFlight(CreateFlightRequest request);

    Optional<Flight> findFlightByCode(String code);

    void updateFlight(String code, FlightStatus status);

    List<Flight> listFlights(FlightStatus status);
}
