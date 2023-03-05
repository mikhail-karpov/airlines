package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FlightInitializingJob {

    private final AirportService airportService;
    private final FlightService flightService;

    public FlightInitializingJob(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    @Scheduled(initialDelayString = "${api.flight-service.schedule-delay}", timeUnit = TimeUnit.SECONDS)
    public void initFlights() {
        log.debug("Initializing flights...");

        try {
            List<Airport> airports = airportService.listAirports();
            Airport origin = getRandomAirport(airports);
            Airport destination = getRandomAirport(airports);

            if (!Objects.equals(origin, destination)) {
                Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());
                log.debug("Created {}", flight);
            }

        } catch (Exception e) {
            log.error("Launching flights failed", e);
        }
    }

    private static Airport getRandomAirport(List<Airport> airports) {
        int originIndex = ThreadLocalRandom.current().nextInt(airports.size());
        return airports.get(originIndex);
    }

}
