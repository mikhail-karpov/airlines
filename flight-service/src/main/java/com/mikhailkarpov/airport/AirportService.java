package com.mikhailkarpov.airport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AirportService {

    private final AirportClient airportClient;

    public AirportService(AirportClient airportClient) {
        this.airportClient = airportClient;
    }

    public List<Airport> listAirports() {
        return airportClient.listAirports();
    }

}
