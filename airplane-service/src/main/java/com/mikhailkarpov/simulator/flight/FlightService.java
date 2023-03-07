package com.mikhailkarpov.simulator.flight;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightService {

    private final FlightClient flightClient;

    public FlightService(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    public Flight createFlight(String origin, String destination) {
        CreateFlightRequest request = CreateFlightRequest.builder()
                .code(UUID.randomUUID().toString())
                .origin(origin)
                .destination(destination)
                .build();

        return flightClient.createFlight(request);
    }

    public Flight getFlight(@NonNull String flightCode) {

        return flightClient.getFlight(flightCode);
    }
}
