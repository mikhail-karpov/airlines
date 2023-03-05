package com.mikhailkarpov.flights.domain;

import com.mikhailkarpov.flights.api.Flight;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FlightRepository extends CrudRepository<FlightEntity, Long> {

    boolean existsByCode(@NonNull String code);

    Optional<FlightEntity> findByCode(String code);

    @Query("SELECT new com.mikhailkarpov.flights.api.Flight(f.code, f.origin, f.destination, f.status) " +
            "FROM FlightEntity f " +
            "WHERE f.code = :code")
    Optional<Flight> findFlightByCode(@NonNull String code);

}
