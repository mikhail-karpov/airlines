package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.airplane.AirplaneCommand;
import com.mikhailkarpov.airplane.AirplaneCommandType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class FlightEventListener {

    private static final String COMMANDS_BINDING = "airplaneCommand-out-0";

    private final StreamBridge streamBridge;

    public FlightEventListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @TransactionalEventListener
    public void handle(@NonNull FlightCreatedEvent event) {
        String flightCode = event.getFlight().getCode();
        AirplaneCommand command = new AirplaneCommand(flightCode, AirplaneCommandType.TAKE_OFF);
        log.debug("Sending {}", command);
        streamBridge.send(COMMANDS_BINDING, command);
    }
}
