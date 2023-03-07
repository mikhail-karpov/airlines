package com.mikhailkarpov.flights.api;

import com.mikhailkarpov.airplane.Airplane;
import com.mikhailkarpov.airplane.AirplaneCommand;
import com.mikhailkarpov.airplane.AirplaneCommandType;
import com.mikhailkarpov.airplane.AirplaneStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.function.Consumer;

import static com.mikhailkarpov.airplane.AirplaneStatus.*;
import static com.mikhailkarpov.flights.api.FlightStatus.*;

@Slf4j
@Component("airplane")
public class FlightDispatcher implements Consumer<Airplane> {

    private final FlightService flightService;
    private final StreamBridge streamBridge;

    public FlightDispatcher(FlightService flightService,
                            StreamBridge streamBridge) {
        this.flightService = flightService;
        this.streamBridge = streamBridge;
    }

    @Override
    public void accept(@NonNull Airplane airplane) {
        log.trace("Received: {}", airplane);
        String flightCode = airplane.getFlightCode();

        flightService.findFlightByCode(flightCode).ifPresent(flight -> {
            AirplaneStatus airplaneStatus = airplane.getStatus();

            if (READY_FOR_TAKE_OFF == airplaneStatus) {
                sendCommand(flightCode, AirplaneCommandType.TAKE_OFF);
            } else if (IN_FLIGHT == airplaneStatus) {
                sendCommand(flightCode, AirplaneCommandType.FLY);
                if (SCHEDULED == flight.getStatus()) {
                    flightService.updateFlight(flightCode, FlightStatus.DEPARTED);
                    log.debug("Flight with code {} departed", flightCode);
                }
            } else if (READY_FOR_LANDING == airplaneStatus) {
                sendCommand(flightCode, AirplaneCommandType.LAND);
            } else if (LANDED == airplaneStatus){
                if (DEPARTED == flight.getStatus()) {
                    flightService.updateFlight(flightCode, ARRIVED);
                    log.debug("Flight with code {} arrived", flightCode);
                }
            }
        });
    }

    @TransactionalEventListener(FlightCreatedEvent.class)
    public void dispatchFlight(@NonNull FlightCreatedEvent event) {
        log.debug("Dispatching flight with code {}", event.getFlightCode());
        sendCommand(event.getFlightCode(), AirplaneCommandType.GET_READY);
    }

    private void sendCommand(String flightCode, AirplaneCommandType commandType) {
        AirplaneCommand command = new AirplaneCommand(flightCode, commandType);
        log.trace("Sending {}", command);
        streamBridge.send("airplaneCommand-out-0", command);
    }
}
