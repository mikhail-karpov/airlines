package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airplane.AirplaneService;
import com.mikhailkarpov.simulator.flight.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class StreamConfig {

    @Bean("flights")
    Consumer<Flight> flightConsumer(AirplaneService airplaneService) {
        return flight -> {
            log.debug("Received {}", flight);
            airplaneService.launchAirplane(flight);
        };
    }
}
