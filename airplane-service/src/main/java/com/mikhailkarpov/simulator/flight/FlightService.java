package com.mikhailkarpov.simulator.flight;

import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    private final FlightClient flightClient;

    public FlightService(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    public Flight getFlight(@NonNull String flightCode) {

        return flightClient.getFlight(flightCode);
    }
}
