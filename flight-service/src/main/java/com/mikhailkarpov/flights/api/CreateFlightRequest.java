package com.mikhailkarpov.flights.api;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder
public class CreateFlightRequest {

    @NotBlank
    String code;

    @NotBlank
    String origin;

    @NotBlank
    String destination;

}
