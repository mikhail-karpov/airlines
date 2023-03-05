package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Configuration
@Profile("demo")
@Slf4j
public class DemoConfig {

    @Bean
    ApplicationRunner flightInitilializer(AirportService airportService, FlightService flightService) {
        return args -> {
            try {
                Thread.sleep(10_000L);
                Random random = new Random();
                List<Airport> airports = airportService.listAirports();
                for (Airport origin : airports) {
                    Airport destination = null;
                    while (destination == null || Objects.equals(origin, destination)) {
                        destination = airports.get(random.nextInt(airports.size()));
                    }
                    Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());
                    log.debug("Created {}", flight);
                }
            } catch (Exception e) {
                log.error("Launching flights failed", e);
            }
        };

    }
}
