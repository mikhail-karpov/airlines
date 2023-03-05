package com.mikhailkarpov.flights.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Flight {

    String code;
    String origin;
    String destination;
    FlightStatus status;

}
