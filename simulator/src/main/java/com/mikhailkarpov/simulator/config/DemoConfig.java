package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile(ApplicationProfile.DEMO)
@Configuration
@EnableScheduling
@Slf4j
public class DemoConfig {

    @Bean
    FlightInitializingJob flightInitializingJob(AirportService airportService, FlightService flightService) {
        return new FlightInitializingJob(airportService, flightService);
    }

}
