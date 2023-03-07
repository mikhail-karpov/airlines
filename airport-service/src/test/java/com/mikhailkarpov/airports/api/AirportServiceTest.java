package com.mikhailkarpov.airports.api;

import com.mikhailkarpov.airports.persistence.AirportEntity;
import com.mikhailkarpov.airports.persistence.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@DataJpaTest
class AirportServiceTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirportRepository airportRepository;

    private AirportService airportService;

    @BeforeEach
    void setUp() {
        this.airportService = new AirportService(airportRepository);
    }

    @Test
    @DisplayName("Should save airport")
    void saveAirport() {
        List<Airport> airports = IntStream.range(0, 1000).mapToObj(i -> {
            Location location = new Location(i, i);
            return new Airport("code " + i, "City " + i, location);
        }).peek(airport -> transactionTemplate.executeWithoutResult(action -> airportService.saveAirport(airport))
        ).collect(Collectors.toList());

        assertIterableEquals(airports, airportService.listAirports());
    }

    @Test
    @DisplayName("Should find airport by code")
    void findAirportByCode() {
        entityManager.persistAndFlush(new AirportEntity("ABC", "City", new Location(1., 2.)));

        Optional<Airport> airportOpt = airportRepository.findAirportByCode("ABC");
        Airport expectedAirport = new Airport("ABC", "City", new Location(1., 2.));

        assertThat(airportOpt).hasValueSatisfying(airport ->
                assertEquals(expectedAirport, airport)
        );
    }

}