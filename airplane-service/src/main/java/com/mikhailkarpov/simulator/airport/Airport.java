package com.mikhailkarpov.simulator.airport;

import lombok.*;

import java.io.Serializable;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder
public class Airport implements Serializable {

    String code;
    String city;
    Location location;

}
