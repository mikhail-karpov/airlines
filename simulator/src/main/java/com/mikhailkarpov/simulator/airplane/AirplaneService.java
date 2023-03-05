package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.airport.Location;
import com.mikhailkarpov.simulator.flight.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AirplaneService {

    private static final String AIRPLANES_BINDING = "airplanes-out-0";

    private final AirplaneRepository airplaneRepository;
    private final AirportService airportService;
    private final StreamBridge streamBridge;
    private Instant nextLaunch = Instant.now();

    public AirplaneService(AirplaneRepository airplaneRepository,
                           AirportService airportService,
                           StreamBridge streamBridge) {
        this.airplaneRepository = airplaneRepository;
        this.airportService = airportService;
        this.streamBridge = streamBridge;
    }

    public List<Airplane> findAllAirplanes() {

        return airplaneRepository.findAll();
    }

    public void launchAirplane(Flight flight) {
        Map<String, Airport> airports = airportService.listAirports().stream()
                .collect(Collectors.toMap(Airport::getCode, Function.identity()));

        Airport origin = airports.get(flight.getOrigin());
        Airport destination = airports.get(flight.getDestination());

        Airplane airplane = Airplane.builder()
                .flightCode(flight.getCode())
                .origin(getPoint(origin.getLocation()))
                .destination(getPoint(destination.getLocation()))
                .speed(500. + 100. * Math.random())
                .build();

        airplaneRepository.save(airplane);
    }

    public void updateAirplanes() {

        airplaneRepository.findAll().forEach(airplane -> {
            update(airplane);
            streamBridge.send(AIRPLANES_BINDING, airplane);
        });

    }

    private void update(Airplane airplane) {
        AirplaneStatus status = airplane.getStatus();

        if (status == AirplaneStatus.LANDED) {
            airplane.takeOff();
            airplaneRepository.save(airplane);
            return;
        }

        //TODO simulate movement
        if (isFlyingTooLong(airplane)) {
            airplane.land();
            airplaneRepository.delete(airplane);
        } else {
            airplane.fly();
            airplaneRepository.save(airplane);
        }
    }

    private boolean isFlyingTooLong(Airplane airplane) {
        Duration inFlightDuration = Duration.between(airplane.getCreatedAt(), Instant.now());
        return inFlightDuration.compareTo(Duration.of(2L, ChronoUnit.MINUTES)) > 0;
    }

    private static Point getPoint(Location location) {
        double x = location.getX();
        double y = location.getY();
        return new Point(x, y);
    }
}
