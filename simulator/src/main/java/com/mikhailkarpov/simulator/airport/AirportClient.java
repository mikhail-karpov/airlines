package com.mikhailkarpov.simulator.airport;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "airportClient", url = "${api.airport-service.url}")
public interface AirportClient {

    @GetMapping("/api/v1/airports")
    List<Airport> getAirports();

}
