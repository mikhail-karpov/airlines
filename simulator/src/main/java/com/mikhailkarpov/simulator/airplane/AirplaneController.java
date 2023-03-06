package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import com.mikhailkarpov.simulator.flight.FlightStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("/api/v1/airplanes")
public class AirplaneController {

    private final AirportService airportService;
    private final AirplaneService airplaneService;
    private final FlightService flightService;

    public AirplaneController(AirportService airportService,
                              AirplaneService airplaneService,
                              FlightService flightService) {
        this.airportService = airportService;
        this.airplaneService = airplaneService;
        this.flightService = flightService;
    }

    @PostMapping
    public Airplane createAirplane() {
        List<Airport> airports = airportService.listAirports();
        Airport origin = getRandomAirport(airports);
        Airport destination = getRandomAirport(airports);
        while (Objects.equals(origin, destination)) {
            destination = getRandomAirport(airports);
        }

        Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());
        log.debug("Created: {}", flight);

        if (flight.getStatus() == FlightStatus.SCHEDULED) {
            Airplane airplane = airplaneService.createAirplane(flight.getCode(), origin.getLocation(), destination.getLocation());
            log.debug("Created: {}", airplane);
            return airplane;
        } else {
            throw new IllegalArgumentException("Unexpected status flight: " + flight.getStatus());
        }
    }

    @GetMapping
    public List<Airplane> listAirplanes() {

        return airplaneService.findAllAirplanes();
    }


    private static Airport getRandomAirport(List<Airport> airports) {
        int originIndex = ThreadLocalRandom.current().nextInt(airports.size());
        return airports.get(originIndex);
    }
}
