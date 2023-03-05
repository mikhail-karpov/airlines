package com.mikhailkarpov.airports.config;

import com.mikhailkarpov.airports.api.Airport;
import com.mikhailkarpov.airports.api.AirportService;
import com.mikhailkarpov.airports.api.Location;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("demo")
public class DemoConfig {

    @Bean
    ApplicationRunner airportInitializer(AirportService airportService) {
        return args -> {
            List.of(
                    new Airport("MUC", "Munich", new Location(48.35, 11.79)),
                    new Airport("LHR", "London", new Location(51.47, -0.46)),
                    new Airport("JFK", "New York", new Location(40.64, -73.78)),
                    new Airport("'DEL'", "Dehli", new Location(28.34, 77.06))
            ).forEach(airportService::saveAirport);
        };
    }
}
