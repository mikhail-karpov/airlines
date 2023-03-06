package com.mikhailkarpov.airplane;

import com.mikhailkarpov.flights.api.FlightService;
import com.mikhailkarpov.flights.api.FlightStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("airplane")
public class AirplaneConsumer implements Consumer<Airplane> {

    private final FlightService flightService;
    private final StreamBridge streamBridge;

    public AirplaneConsumer(FlightService flightService, StreamBridge streamBridge) {
        this.flightService = flightService;
        this.streamBridge = streamBridge;
    }

    @Override
    public void accept(@NonNull Airplane airplane) {
        log.debug("Received: {}", airplane);

        String flightCode = airplane.getFlightCode();

        flightService.findFlightByCode(flightCode).ifPresent(flight -> {
            FlightStatus flightStatus = flight.getStatus();

            switch (airplane.getStatus()) {
                case IN_FLIGHT:
                    if (flightStatus == FlightStatus.SCHEDULED) {
                        flightService.updateFlight(flightCode, FlightStatus.DEPARTED);
                    }
                    AirplaneCommand command = new AirplaneCommand(flightCode, AirplaneCommandType.FLY);
                    log.debug("Sending {}", command);
                    streamBridge.send("airplaneCommand-out-0", command);
                    //TODO airplane position notification
                    break;
                case READY_FOR_LANDING:
                    if (flightStatus != FlightStatus.ARRIVED) {
                        flightService.updateFlight(flightCode, FlightStatus.ARRIVED);
                    }
                    command = new AirplaneCommand(flightCode, AirplaneCommandType.LAND);
                    log.debug("Sending {}", command);
                    streamBridge.send("airplaneCommand-out-0", command);
                    break;
                default:
                    break;
            }
        });
    }

}
