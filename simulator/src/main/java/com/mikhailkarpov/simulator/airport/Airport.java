package com.mikhailkarpov.simulator.airport;

import lombok.*;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder
public class Airport {

    String code;
    String city;
    Location location;

}
