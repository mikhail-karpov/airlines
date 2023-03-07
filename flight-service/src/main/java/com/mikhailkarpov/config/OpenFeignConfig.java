package com.mikhailkarpov.config;

import com.mikhailkarpov.airport.AirportClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AirportClient.class})
public class OpenFeignConfig {

}
