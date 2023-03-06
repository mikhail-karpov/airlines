package com.mikhailkarpov.airplane;

import lombok.NonNull;
import lombok.Value;

@Value
public class AirplaneCommand {

    @NonNull String flightCode;
    @NonNull AirplaneCommandType commandType;

}
