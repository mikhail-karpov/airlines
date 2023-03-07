package com.mikhailkarpov.airport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class Location {

    double x, y;

    @Override
    public String toString() {
        return String.format("Location{x=%.2f, y=%.2f}", x, y);
    }
}
