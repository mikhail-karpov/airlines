package com.mikhailkarpov.simulator.airplane;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.Objects;

@RedisHash("airplanes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Airplane {

    @Id
    private String flightCode;

    private Point origin;
    private Point destination;
    private Point location;
    private AirplaneStatus status;
    private double speed;
    private Instant updatedAt;

    @Builder
    public Airplane(@NonNull String flightCode, @NonNull Point origin, @NonNull Point destination, double speed) {
        this.flightCode = flightCode;
        this.origin = origin;
        this.destination = destination;
        this.location = origin;
        this.status = AirplaneStatus.ON_GROUND;
        this.updatedAt = Instant.now();
        setSpeed(speed);
    }

    public void takeOff() {
        checkStatus(AirplaneStatus.ON_GROUND);
        this.status = AirplaneStatus.IN_FLIGHT;
        this.updatedAt = Instant.now();
    }

    public void fly() {
        checkStatus(AirplaneStatus.IN_FLIGHT);
        Instant now = Instant.now();
        long timeElapsed = now.toEpochMilli() - updatedAt.toEpochMilli();
        this.updatedAt = Instant.now();
    }

    public void land() {
        checkStatus(AirplaneStatus.IN_FLIGHT);
        this.status = AirplaneStatus.ON_GROUND;
        this.location = destination;
        this.updatedAt = Instant.now();
    }

    private void setSpeed(double speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        this.speed = speed;
    }

    private void checkStatus(AirplaneStatus requiredStatus) {
        if (status != requiredStatus) {
            throw new IllegalStateException("Airplane code=" + flightCode + " illegal status: " + status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airplane airplane = (Airplane) o;

        return Objects.equals(flightCode, airplane.flightCode);
    }

    @Override
    public int hashCode() {
        return flightCode != null ? flightCode.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Airplane{" +
                "flightCode='" + flightCode + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", location=" + location +
                ", status=" + status +
                ", speed=" + speed +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
