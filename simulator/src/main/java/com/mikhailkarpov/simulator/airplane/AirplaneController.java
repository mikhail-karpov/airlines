package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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

    @GetMapping
    public List<Airplane> listAirplanes() {

        return airplaneService.findAllAirplanes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createFlight() {
        List<Airport> airports = airportService.listAirports();
        Airport origin = getRandomAirport(airports);
        Airport destination = getRandomAirport(airports);

        while (Objects.equals(origin, destination)) {
            destination = getRandomAirport(airports);
        }

        flightService.createFlight(origin.getCode(), destination.getCode());
    }

    private static Airport getRandomAirport(List<Airport> airports) {
        int originIndex = ThreadLocalRandom.current().nextInt(airports.size());
        return airports.get(originIndex);
    }
}
