package com.mikhailkarpov.simulator.airplane;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@RedisHash("airplanes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class Airplane {

    @Id
    private String flightCode;

    private Point location;
    private Point destination;
    private AirplaneStatus status;
    private double speed;
    private Instant tookOffAt;
    private Instant updatedAt;

    @Builder
    public Airplane(@NonNull String flightCode, @NonNull Point location, @NonNull Point destination, double speed) {
        this.flightCode = flightCode;
        this.destination = destination;
        this.location = location;
        this.status = AirplaneStatus.READY_FOR_TAKE_OFF;
        this.updatedAt = Instant.now();
        setSpeed(speed);
    }

    public void takeOff() {
        checkStatus(AirplaneStatus.READY_FOR_TAKE_OFF);
        this.status = AirplaneStatus.IN_FLIGHT;
        this.updatedAt = Instant.now();
        this.tookOffAt = this.updatedAt;
        log.debug("Took off: {}", this);
    }

    public void fly() {
        checkStatus(AirplaneStatus.IN_FLIGHT);
        this.updatedAt = Instant.now();
        if (isFlyingTooLong()) {
            //TODO real movement
            this.status = AirplaneStatus.READY_FOR_LANDING;
        }
        log.debug("Flying {}", this);
    }

    public void land() {
        checkStatus(AirplaneStatus.READY_FOR_LANDING);
        this.status = AirplaneStatus.READY_FOR_TAKE_OFF;
        this.location = destination;
        this.updatedAt = Instant.now();
        log.debug("Landing {}", this);
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

    private boolean isFlyingTooLong() {
        if (tookOffAt == null) {
            return false;
        }
        return Instant.now().isAfter(tookOffAt.plus(10L, ChronoUnit.SECONDS));
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
                ", destination=" + destination +
                ", location=" + location +
                ", status=" + status +
                ", speed=" + speed +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
