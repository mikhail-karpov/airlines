package com.mikhailkarpov.simulator.airplane;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AirplaneRepository extends CrudRepository<Airplane, String> {

    List<Airplane> findAll();
}
