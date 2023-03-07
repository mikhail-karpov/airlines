package com.mikhailkarpov.simulator.airplane.command;

import lombok.*;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class AirplaneCommand {

    @NonNull String flightCode;
    @NonNull AirplaneCommandType commandType;

}
