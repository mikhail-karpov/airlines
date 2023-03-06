package com.mikhailkarpov.simulator.airplane;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("airplaneCommand")
@Slf4j
public class AirplaneCommandHandler implements Function<AirplaneCommand, Airplane> {

    private final AirplaneService airplaneService;

    public AirplaneCommandHandler(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @Override
    public Airplane apply(AirplaneCommand command) {
        log.debug("Received {}", command);
        return updateAirplane(command.getFlightCode(), command.getCommandType());
    }

    private Airplane updateAirplane(String flightCode, AirplaneCommandType commandType) {
        if (AirplaneCommandType.TAKE_OFF == commandType) {
            return airplaneService.takeOff(flightCode);
        } else if (AirplaneCommandType.LAND == commandType) {
            return airplaneService.land(flightCode);
        } else {
            return airplaneService.fly(flightCode);
        }
    }

}
