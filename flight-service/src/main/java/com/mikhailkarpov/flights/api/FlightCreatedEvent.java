package com.mikhailkarpov.flights.api;

import lombok.NonNull;
import lombok.Value;

@Value
public class FlightCreatedEvent {

    @NonNull String flightCode;
}
