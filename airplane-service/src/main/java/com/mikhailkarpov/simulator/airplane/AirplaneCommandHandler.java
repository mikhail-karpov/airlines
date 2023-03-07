package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airplane.command.AirplaneCommand;
import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("airplaneCommand")
@Slf4j
@RequiredArgsConstructor
public class AirplaneCommandHandler implements Function<AirplaneCommand, Airplane> {

    private final AirportService airportService;
    private final AirplaneService airplaneService;
    private final FlightService flightService;

    @Override
    public Airplane apply(AirplaneCommand command) {
        log.trace("Received {}", command);

        String flightCode = command.getFlightCode();

        switch (command.getCommandType()) {
            case GET_READY:
                return createAirplane(flightCode);
            case TAKE_OFF:
                return airplaneService.takeOff(flightCode);
            case LAND:
                return airplaneService.land(flightCode);
            default:
                return airplaneService.fly(flightCode);
        }
    }

    private Airplane createAirplane(String flightCode) {
        log.debug("Creating airplane for the flight with code " + flightCode);
        Flight flight = flightService.getFlight(flightCode);
        Airport origin = airportService.getAirport(flight.getOrigin());
        Airport destination = airportService.getAirport(flight.getDestination());
        return airplaneService.createAirplane(flight.getCode(), origin.getLocation(), destination.getLocation());
    }

}
