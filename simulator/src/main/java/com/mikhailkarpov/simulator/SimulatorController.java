package com.mikhailkarpov.simulator;

import com.mikhailkarpov.simulator.airplane.Airplane;
import com.mikhailkarpov.simulator.airplane.AirplaneCommand;
import com.mikhailkarpov.simulator.airplane.AirplaneCommandType;
import com.mikhailkarpov.simulator.airplane.AirplaneService;
import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController("airplaneCommand")
@RequestMapping("/api/v1/airplanes")
public class SimulatorController implements Function<AirplaneCommand, Airplane> {

    private final AirportService airportService;
    private final AirplaneService airplaneService;
    private final FlightService flightService;

    public SimulatorController(AirportService airportService,
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

        if (AirplaneCommandType.GET_READY == commandType) {
            return launchAirplane(flightCode);
        } else if (AirplaneCommandType.TAKE_OFF == commandType) {
            return airplaneService.takeOff(flightCode);
        } else if (AirplaneCommandType.LAND == commandType) {
            return airplaneService.land(flightCode);
        } else {
            return airplaneService.fly(flightCode);
        }
    }

    private Airplane launchAirplane(String flightCode) {
        Flight flight = flightService.getFlight(flightCode);

        Map<String, Airport> airports = airportService.listAirports()
                .stream()
                .collect(Collectors.toMap(Airport::getCode, Function.identity()));

        Airport origin = airports.get(flight.getOrigin());
        Airport destination = airports.get(flight.getDestination());
        if (origin == null || destination == null) {
            String errMessage = String.format("Unable to create flight %s -> %s", flight.getOrigin(), flight.getDestination());
            throw new IllegalArgumentException(errMessage);
        }

        Airplane airplane = airplaneService.createAirplane(flight.getCode(), origin.getLocation(), destination.getLocation());
        log.debug("Created: {}", airplane);
        return airplane;
    }

    @PostMapping
    public Flight createRandomFlight() {
        List<Airport> airports = airportService.listAirports();
        Airport origin = getRandomAirport(airports);
        Airport destination = getRandomAirport(airports);
        while (Objects.equals(origin, destination)) {
            destination = getRandomAirport(airports);
        }

        Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());
        log.debug("Created random flight: {}", flight);
        return flight;
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
