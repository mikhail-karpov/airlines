package com.mikhailkarpov.airplane;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Airplane {

    String flightCode;
    Location origin;
    Location destination;
    Location location;
    AirplaneStatus status;
    Double speed;
    Instant updatedAt;


}
