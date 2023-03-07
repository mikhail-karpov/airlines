package com.mikhailkarpov.airplane;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AirplaneCommandType {

    GET_READY,

    TAKE_OFF,

    @JsonEnumDefaultValue
    FLY,

    LAND
}
