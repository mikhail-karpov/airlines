package com.mikhailkarpov.simulator.airport;

import com.mikhailkarpov.simulator.config.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AirportService {

    private final AirportClient airportClient;

    public AirportService(AirportClient airportClient) {
        this.airportClient = airportClient;
    }

    @Cacheable(cacheNames = CacheConfig.AIRPORT_CACHE, key = "#airportCode")
    public Airport getAirport(String airportCode) {
        try {
            return airportClient.getAirport(airportCode);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Airport not found by code " + airportCode);
        }
    }
}
