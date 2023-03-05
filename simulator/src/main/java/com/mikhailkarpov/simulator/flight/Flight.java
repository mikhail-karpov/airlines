package com.mikhailkarpov.simulator.flight;

import lombok.*;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder
public class Flight {

    String code;
    String origin;
    String destination;
    FlightStatus status;

}
