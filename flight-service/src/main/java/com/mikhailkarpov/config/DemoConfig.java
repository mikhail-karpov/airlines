package com.mikhailkarpov.config;

import com.mikhailkarpov.airport.Airport;
import com.mikhailkarpov.airport.AirportService;
import com.mikhailkarpov.flights.api.CreateFlightRequest;
import com.mikhailkarpov.flights.api.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Profile("demo")
@Configuration
@EnableScheduling
@Slf4j
public class DemoConfig {

    private final AirportService airportService;
    private final FlightService flightService;

    public DemoConfig(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    @Scheduled(fixedDelayString = "${demo.random-flight-scheduling.delay}", timeUnit = TimeUnit.SECONDS)
    public void createRandomFlight() {
        log.debug("Creating random flight...");
        List<Airport> airports = airportService.listAirports();
        Airport origin = getRandomAirport(airports);
        Airport destination = getRandomAirport(airports);
        while (Objects.equals(origin, destination)) {
            destination = getRandomAirport(airports);
        }

        CreateFlightRequest request = new CreateFlightRequest(
                UUID.randomUUID().toString(),
                origin.getCode(),
                destination.getCode()
        );
        flightService.createFlight(request);
    }

    private static Airport getRandomAirport(List<Airport> airports) {
        if (airports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No airports found");
        }
        int originIndex = ThreadLocalRandom.current().nextInt(airports.size());
        return airports.get(originIndex);
    }
}
