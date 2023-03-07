package com.mikhailkarpov.airport;

import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "airportClient", url = "${api.airport-service.url}")
public interface AirportClient {

    @GetMapping("/api/v1/airports")
    List<Airport> listAirports();

    @GetMapping("/api/v1/airports/{code}")
    Airport getAirport(@PathVariable @NonNull String code);

}
