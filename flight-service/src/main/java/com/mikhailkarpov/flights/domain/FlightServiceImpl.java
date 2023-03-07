package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.*;
import com.mikhailkarpov.flights.api.exceptions.FlightAlreadyExistsException;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FlightServiceImpl(FlightRepository flightRepository, ApplicationEventPublisher eventPublisher) {
        this.flightRepository = flightRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Flight createFlight(@NonNull CreateFlightRequest request) {
        if (flightRepository.existsByCode(request.getCode())) {
            throw new FlightAlreadyExistsException(request.getCode());
        }

        Flight flight = saveFlight(request);
        eventPublisher.publishEvent(new FlightCreatedEvent(flight.getCode()));
        return flight;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> findFlightByCode(@NonNull String code) {

        return flightRepository.findFlightByCode(code);
    }

    @Override
    public List<Flight> listFlights() {

        return flightRepository.findAllFlights();
    }

    @Override
    @Transactional
    public void updateFlight(@NonNull String code, @NonNull FlightStatus status) {

        flightRepository.findByCode(code).ifPresent(flight ->
            flight.updateStatus(status)
        );
    }

    private Flight saveFlight(@NotNull CreateFlightRequest request) {
        FlightEntity flightEntity = flightRepository.save(FlightEntity.builder()
                .code(request.getCode())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(FlightStatus.SCHEDULED)
                .build()
        );

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
