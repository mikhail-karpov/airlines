package com.mikhailkarpov.simulator.flight;

import com.mikhailkarpov.simulator.airplane.Airplane;
import com.mikhailkarpov.simulator.airplane.AirplaneService;
import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class FlightInitializingJob {

    private final AirportService airportService;
    private final AirplaneService airplaneService;
    private final FlightService flightService;

    public FlightInitializingJob(AirportService airportService,
                                 AirplaneService airplaneService,
                                 FlightService flightService) {
        this.airportService = airportService;
        this.airplaneService = airplaneService;
        this.flightService = flightService;
    }

    @Scheduled(cron = "${api.flight-service.schedule-cron}")
    public void initFlights() {
        log.debug("Initializing flights...");

        try {
            List<Airport> airports = airportService.listAirports();
            Airport origin = getRandomAirport(airports);
            Airport destination = getRandomAirport(airports);

            if (!Objects.equals(origin, destination)) {
                Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());
                Airplane airplane = airplaneService.launchAirplane(flight.getCode(), origin, destination);
                log.debug("Launched: {}", airplane);
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
