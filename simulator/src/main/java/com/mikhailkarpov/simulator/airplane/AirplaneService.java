package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Airport;
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

    public Airplane launchAirplane(@NonNull String flightCode, @NonNull Airport origin, @NonNull Airport destination) {
        Airplane airplane = Airplane.builder()
                .flightCode(flightCode)
                .speed(500. + 100. * Math.random())
                .origin(getPoint(origin.getLocation()))
                .destination(getPoint(destination.getLocation()))
                .build();

        return airplaneRepository.save(airplane);
    }

    private static Point getPoint(Location location) {
        return new Point(location.getX(), location.getY());
    }
}
