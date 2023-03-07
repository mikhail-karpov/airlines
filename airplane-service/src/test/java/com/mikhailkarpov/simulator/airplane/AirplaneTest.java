package com.mikhailkarpov.simulator.airplane;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AirplaneTest {

    private static final Logger log = LoggerFactory.getLogger(AirplaneTest.class);

    @Test
    @DisplayName("Should take off, fly and land at a destination")
    void testFlying() {
        //given
        Point location = new Point(-54.5, 75.3);
        Point destination = new Point(151.4, -84.3);
        Airplane airplane = new Airplane("flightCode", location, destination, 500.);

        //when
        airplane.takeOff();
        Instant tookOffAt = Instant.now();
        while (airplane.getStatus() != AirplaneStatus.READY_FOR_LANDING) {
            airplane.fly();
        }
        airplane.land();
        Instant landedAt = Instant.now();
        log.info("Flight duration: {} ms", landedAt.toEpochMilli() - tookOffAt.toEpochMilli());

        //then
        assertEquals(destination, airplane.getLocation());
        assertEquals(AirplaneStatus.READY_FOR_TAKE_OFF, airplane.getStatus());
    }
}