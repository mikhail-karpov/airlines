package com.mikhailkarpov.simulator.airplane;

import com.mikhailkarpov.simulator.airport.Airport;
import com.mikhailkarpov.simulator.airport.AirportService;
import com.mikhailkarpov.simulator.airport.Location;
import com.mikhailkarpov.simulator.flight.Flight;
import com.mikhailkarpov.simulator.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.geo.Point;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class AirplaneUpdateJob {

    private final AirplaneService airplaneService;
    private final AirportService airportService;
    private final FlightService flightService;
    private final StreamBridge streamBridge;
    private Instant nextLaunch = Instant.now();

    public AirplaneUpdateJob(AirportService airportService,
                             AirplaneService airplaneService,
                             FlightService flightService,
                             StreamBridge streamBridge) {
        this.airportService = airportService;
        this.airplaneService = airplaneService;
        this.flightService = flightService;
        this.streamBridge = streamBridge;
    }

    @Scheduled(cron = "${api.airplane-service.schedule-cron}")
    public void updateAirplanes() {

        airplaneService.findAllAirplanes().forEach(airplane -> {
            update(airplane);
            sendUpdate(airplane);
        });

        if (Instant.now().isAfter(nextLaunch)) {
            createFlight();
            long seconds = ThreadLocalRandom.current().nextLong(15L);
            nextLaunch = Instant.now().plus(seconds, ChronoUnit.SECONDS);
        }
    }

    private void update(Airplane airplane) {
        AirplaneStatus status = airplane.getStatus();

        if (status == AirplaneStatus.LANDED) {
            airplane.takeOff();
            airplaneService.save(airplane);
            return;
        }

        //TODO simulate movement
        if (isFlyingTooLong(airplane)) {
            airplane.land();
            airplaneService.delete(airplane);
        } else {
            airplane.fly();
            airplaneService.save(airplane);
        }
    }

    private void sendUpdate(Airplane airplane) {
        Message<Airplane> message = MessageBuilder.withPayload(airplane)
                .build();
        streamBridge.send("airplanes-out-0", message);
    }

    private boolean isFlyingTooLong(Airplane airplane) {
        Duration inFlightDuration = Duration.between(airplane.getCreatedAt(), Instant.now());
        return inFlightDuration.compareTo(Duration.of(2L, ChronoUnit.MINUTES)) > 0;
    }

    public void createFlight() {
        log.debug("Initializing flights...");

        try {
            List<Airport> airports = airportService.listAirports();
            Airport origin = getRandomAirport(airports);
            Airport destination = getRandomAirport(airports);

            if (!Objects.equals(origin, destination)) {
                Flight flight = flightService.createFlight(origin.getCode(), destination.getCode());

                Airplane airplane = Airplane.builder()
                        .flightCode(flight.getCode())
                        .speed(500. + 100. * Math.random())
                        .origin(getPoint(origin.getLocation()))
                        .destination(getPoint(destination.getLocation()))
                        .build();

                airplaneService.save(airplane);
                log.debug("Scheduled flight: {}", flight);
                log.debug("Scheduled airplane: {}", airplane);
            }
        } catch (Exception e) {
            log.error("Launching flights failed", e);
        }
    }

    private static Airport getRandomAirport(List<Airport> airports) {
        int originIndex = ThreadLocalRandom.current().nextInt(airports.size());
        return airports.get(originIndex);
    }

    private static Point getPoint(Location location) {
        return new Point(location.getX(), location.getY());
    }
}
