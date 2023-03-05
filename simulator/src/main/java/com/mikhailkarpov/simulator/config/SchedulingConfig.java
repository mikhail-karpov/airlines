package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airplane.AirplaneService;
import com.mikhailkarpov.simulator.airplane.AirplaneUpdateJob;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.FlightService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    AirplaneUpdateJob airplaneUpdateJob(AirplaneService airplaneService,
                                        AirportService airportService,
                                        FlightService flightService,
                                        StreamBridge streamBridge) {
        return new AirplaneUpdateJob(airportService, airplaneService, flightService, streamBridge);
    }
}
