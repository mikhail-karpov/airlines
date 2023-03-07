package com.mikhailkarpov.simulator.flight;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flightClient", url = "${api.flight-service.url}")
public interface FlightClient {

    @GetMapping("/api/v1/flights/{code}")
    Flight getFlight(@PathVariable String code);

}
