package com.mikhailkarpov.simulator.flight;

import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "flightClient", url = "${api.flight-service.url}")
public interface FlightClient {

    @PostMapping("/api/v1/flights")
    Flight createFlight(@NonNull CreateFlightRequest request);

    @GetMapping("/api/v1/flights/{code}")
    Flight getFlight(@PathVariable @NonNull String code);

}
