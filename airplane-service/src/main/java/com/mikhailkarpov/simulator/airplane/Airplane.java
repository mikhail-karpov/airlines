package com.mikhailkarpov.simulator.airplane;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@RedisHash("airplanes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    }

    public void fly() {
        checkStatus(AirplaneStatus.IN_FLIGHT);
        updateLocation();
        this.updatedAt = Instant.now();
    }

    public void land() {
        checkStatus(AirplaneStatus.READY_FOR_LANDING);
        this.status = AirplaneStatus.LANDED;
        this.location = destination;
        this.updatedAt = Instant.now();
    }

    private void setSpeed(double speed) {
        if (speed < 100. || speed > 2000.) {
            throw new IllegalArgumentException("Speed must be between 100 and 2000 km/h");
        }
        this.speed = speed;
    }

    private void checkStatus(AirplaneStatus... requiredStatus) {
        if (Arrays.stream(requiredStatus).noneMatch(status -> status == this.status)) {
            throw new IllegalStateException("Airplane code=" + flightCode + " illegal status: " + status);
        }
    }

    private void updateLocation() {
        double deltaX = destination.getX() - location.getX();
        double deltaY = destination.getY() - location.getY();
        double distanceToFly = speed / 36000. * (Instant.now().toEpochMilli() - updatedAt.toEpochMilli());
        double distanceToDestination = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distanceToFly > distanceToDestination) {
            location = new Point(destination);
            status = AirplaneStatus.READY_FOR_LANDING;
        } else {
            double direction = Math.atan2(deltaY, deltaX);
            double x = location.getX() + distanceToFly * Math.cos(direction);
            double y = location.getY() + distanceToFly * Math.sin(direction);
            this.location = new Point(x, y);
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
                ", destination=" + destination +
                ", location=" + location +
                ", status=" + status +
                ", speed=" + speed +
                ", updatedAt=" + updatedAt +
                '}';
    }


}
