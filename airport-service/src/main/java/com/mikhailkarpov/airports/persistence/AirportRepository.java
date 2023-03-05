package com.mikhailkarpov.airports.persistence;

import com.mikhailkarpov.airports.api.Airport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AirportRepository extends CrudRepository<AirportEntity, Long> {

    @Query("SELECT new com.mikhailkarpov.airports.api.Airport(a.code, a.city, a.location) " +
            "FROM AirportEntity a")
    List<Airport> findAllAirports();
}
