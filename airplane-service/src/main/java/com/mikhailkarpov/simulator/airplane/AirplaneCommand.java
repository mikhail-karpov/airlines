package com.mikhailkarpov.simulator.airplane;

import lombok.*;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class AirplaneCommand {

    @NonNull String flightCode;
    @NonNull AirplaneCommandType commandType;

}
