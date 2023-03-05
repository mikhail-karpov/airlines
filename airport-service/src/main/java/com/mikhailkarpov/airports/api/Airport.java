package com.mikhailkarpov.airports.api;

import lombok.Value;

@Value
public class Airport {

    String code;
    String city;
    Location location;

}
