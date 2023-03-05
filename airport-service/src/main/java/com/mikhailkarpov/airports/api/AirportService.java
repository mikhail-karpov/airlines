package com.mikhailkarpov.airports.api;

import com.mikhailkarpov.airports.persistence.AirportEntity;
import com.mikhailkarpov.airports.persistence.AirportRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Transactional
    public void saveAirport(@NonNull Airport airport) {
        log.debug("Saving {}", airport);

        AirportEntity airportEntity = AirportEntity.builder()
                .code(airport.getCode())
                .city(airport.getCity())
                .location(airport.getLocation())
                .build();

        airportRepository.save(airportEntity);
    }

    @Transactional(readOnly = true)
    public List<Airport> listAirports() {

        return airportRepository.findAllAirports();
    }
}
