package com.mikhailkarpov.flights.api;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    Flight createFlight(CreateFlightRequest request);

    Optional<Flight> findFlightByCode(String code);

    List<Flight> listFlights();

    void updateFlight(String code, FlightStatus status);

}
