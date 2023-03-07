package com.mikhailkarpov.simulator.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
@AllArgsConstructor
@Builder
public class CreateFlightRequest {

    @NonNull String code;
    @NonNull String origin;
    @NonNull String destination;

}
