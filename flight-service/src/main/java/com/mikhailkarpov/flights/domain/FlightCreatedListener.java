package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.Flight;
import lombok.NonNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FlightCreatedListener {

    private static final String FLIGHT_BINDING = "flights-out-0";

    private final StreamBridge streamBridge;

    public FlightCreatedListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @TransactionalEventListener
    public void handle(@NonNull FlightCreatedEvent event) {
        Message<Flight> message = MessageBuilder.withPayload(event.getFlight()).build();
        streamBridge.send(FLIGHT_BINDING, message);
    }
}
