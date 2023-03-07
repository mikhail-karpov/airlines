package com.mikhailkarpov.simulator.airport;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportClient airportClient;

    public AirportService(AirportClient airportClient) {
        this.airportClient = airportClient;
    }

    public List<Airport> listAirports() {
        return airportClient.getAirports();
    }
}
