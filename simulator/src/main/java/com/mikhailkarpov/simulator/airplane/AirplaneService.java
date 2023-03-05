package com.mikhailkarpov.simulator.airplane;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;

    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public void save(Airplane airplane) {
        airplaneRepository.save(airplane);
    }

    public List<Airplane> findAllAirplanes() {

        return airplaneRepository.findAll();
    }

    public void delete(Airplane airplane) {

        airplaneRepository.delete(airplane);
    }
}
