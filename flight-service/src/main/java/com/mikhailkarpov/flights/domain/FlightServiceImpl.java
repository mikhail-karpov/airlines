package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.CreateFlightRequest;
import com.mikhailkarpov.flights.api.Flight;
import com.mikhailkarpov.flights.api.FlightService;
import com.mikhailkarpov.flights.api.FlightStatus;
import com.mikhailkarpov.flights.api.exceptions.FlightAlreadyExistsException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FlightServiceImpl(FlightRepository flightRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.flightRepository = flightRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Flight createFlight(@NonNull CreateFlightRequest request) {
        Flight flight = createAndSaveFlight(request);
        eventPublisher.publishEvent(new FlightCreatedEvent(flight));
        return flight;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> findFlightByCode(@NonNull String code) {

        return flightRepository.findFlightByCode(code);
    }

    @Override
    @Transactional
    public void updateFlight(@NonNull String code, @NonNull FlightStatus status) {

        flightRepository.findByCode(code).ifPresent(flight -> {
            flight.updateStatus(status);
            log.debug("Updating flight: {}", flight);
        });
    }

    private Flight createAndSaveFlight(CreateFlightRequest request) {
        if (flightRepository.existsByCode(request.getCode())) {
            throw new FlightAlreadyExistsException(request.getCode());
        }

        FlightEntity flightEntity = flightRepository.save(FlightEntity.builder()
                .code(request.getCode())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(FlightStatus.SCHEDULED)
                .build());

        return mapEntityToDto(flightEntity);
    }

    private static Flight mapEntityToDto(FlightEntity entity) {
        return Flight.builder()
                .code(entity.getCode())
                .origin(entity.getOrigin())
                .destination(entity.getDestination())
                .status(entity.getStatus())
                .build();
    }
}
