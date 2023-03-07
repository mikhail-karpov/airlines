package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Location;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public Airplane createAirplane(@NonNull String flightCode, @NonNull Location origin, @NonNull Location destination) {
        if (airplaneRepository.existsById(flightCode)) {
            throw new RuntimeException("Airplane already exists with code " + flightCode);
        }

        Airplane airplane = Airplane.builder()
                .flightCode(flightCode)
                .location(getPoint(origin))
                .destination(getPoint(destination))
                .speed(500. + 100. * Math.random())
                .build();

        log.debug("Creating: {}", airplane);
        return airplaneRepository.save(airplane);
    }

    public Airplane takeOff(String flightCode) {
        Airplane airplane = airplaneRepository.findById(flightCode).orElseThrow();
        airplane.takeOff();
        log.debug("Taking off: {}", airplane);
        return airplaneRepository.save(airplane);
    }

    public Airplane fly(String flightCode) {
        Airplane airplane = airplaneRepository.findById(flightCode).orElseThrow();
        airplane.fly();
        log.trace("Flying {}", airplane);
        return airplaneRepository.save(airplane);
    }

    public Airplane land(String flightCode) {
        Airplane airplane = airplaneRepository.findById(flightCode).orElseThrow();
        airplane.land();
        log.debug("Landing: {}", airplane);
        return airplaneRepository.save(airplane);
    }

    private static Point getPoint(Location location) {
        double x = location.getX();
        double y = location.getY();
        return new Point(x, y);
    }

}
