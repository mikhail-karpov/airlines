package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.Flight;
import lombok.NonNull;
import lombok.Value;

@Value
public class FlightCreatedEvent {

    @NonNull Flight flight;

}
