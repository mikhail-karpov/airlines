package com.mikhailkarpov.simulator.airport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Location implements Serializable {

    double x, y;

    @Override
    public String toString() {
        return String.format("Location{x=%.2f, y=%.2f}", x, y);
    }
}
