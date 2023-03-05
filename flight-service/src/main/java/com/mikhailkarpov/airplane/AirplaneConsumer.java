package com.mikhailkarpov.airplane;

import com.mikhailkarpov.flights.api.FlightService;
import com.mikhailkarpov.flights.api.FlightStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("airplanes")
public class AirplaneConsumer implements Consumer<Airplane> {

    private final FlightService flightService;

    public AirplaneConsumer(FlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public void accept(@NonNull Airplane airplane) {
        log.debug("Received: {}", airplane);

        String flightCode = airplane.getFlightCode();

        flightService.findFlightByCode(flightCode).ifPresent(flight -> {
            FlightStatus flightStatus = flight.getStatus();

            switch (airplane.getStatus()) {
                case TOOK_OFF:
                    if (flightStatus == FlightStatus.SCHEDULED) {
                        flightService.updateFlight(flightCode, FlightStatus.DEPARTED);
                    }
                    break;
                case IN_FLIGHT:
                    if (flightStatus != FlightStatus.DEPARTED) {
                        flightService.updateFlight(flightCode, FlightStatus.DEPARTED);
                    }
                    //TODO notify location
                    break;
                case LANDED:
                    if (flightStatus != FlightStatus.ARRIVED) {
                        flightService.updateFlight(flightCode, FlightStatus.ARRIVED);
                    }
                    break;
                default:
                    break;
            }
        });
    }
}
