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
import java.util.function.Function;

@Slf4j
@RestController("airplaneCommand")
@RequestMapping("/api/v1/airplanes")
public class AirplaneController implements Function<AirplaneCommand, Airplane> {

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

    @Override
    public Airplane apply(AirplaneCommand command) {
        log.debug("Received {}", command);

        String flightCode = command.getFlightCode();
        AirplaneCommandType commandType = command.getCommandType();

        if (AirplaneCommandType.TAKE_OFF == commandType) {
            return airplaneService.takeOff(flightCode);
        } else if (AirplaneCommandType.LAND == commandType) {
            return airplaneService.land(flightCode);
        } else {
            return airplaneService.fly(flightCode);
        }
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

        if (flight.getStatus() == FlightStatus.CREATED) {
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
