package com.mikhailkarpov.simulator.airplane;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AirplaneCommandType {

    TAKE_OFF,

    @JsonEnumDefaultValue
    FLY,

    LAND
}
