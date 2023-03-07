package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airport.AirportClient;
import com.mikhailkarpov.simulator.flight.FlightClient;
import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
        AirportClient.class,
        FlightClient.class
})
public class OpenFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
