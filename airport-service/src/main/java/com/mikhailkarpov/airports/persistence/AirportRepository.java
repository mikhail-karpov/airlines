package com.mikhailkarpov.airports.persistence;

import com.mikhailkarpov.airports.api.Airport;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends CrudRepository<AirportEntity, Long> {

    @Query("SELECT new com.mikhailkarpov.airports.api.Airport(a.code, a.city, a.location) " +
            "FROM AirportEntity a")
    List<Airport> findAllAirports();

    @Query("SELECT new com.mikhailkarpov.airports.api.Airport(a.code, a.city, a.location) " +
            "FROM AirportEntity a " +
            "WHERE a.code = :code")
    Optional<Airport> findAirportByCode(@NonNull String code);
}
