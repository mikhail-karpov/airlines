package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.CreateFlightRequest;
import com.mikhailkarpov.flights.api.Flight;
import com.mikhailkarpov.flights.api.FlightService;
import com.mikhailkarpov.flights.api.FlightStatus;
import com.mikhailkarpov.flights.api.exceptions.FlightAlreadyExistsException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    @Transactional
    public Flight createFlight(CreateFlightRequest request) {

        if (flightRepository.existsByCode(request.getCode())) {
            throw new FlightAlreadyExistsException(request.getCode());
        }

        FlightEntity flightEntity = flightRepository.save(FlightEntity.builder()
                .code(request.getCode())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(FlightStatus.SCHEDULED)
                .build());

        log.debug("Creating flight: {}", flightEntity);
        return mapEntityToDto(flightEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flight> findFlightByCode(@NonNull String code) {

        return flightRepository.findFlightByCode(code);
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
