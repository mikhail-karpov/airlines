package com.mikhailkarpov.airports.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;

@Value
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Location {

    double x, y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Location{x=%.2f, y=%.2f}", x, y);
    }
}
