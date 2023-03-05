package com.mikhailkarpov.simulator.flight;

import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "flightClient", url = "${api.flight-service.url}")
public interface FlightClient {

    @PostMapping("/api/v1/flights")
    Flight createFlight(@NonNull CreateFlightRequest request);

}
