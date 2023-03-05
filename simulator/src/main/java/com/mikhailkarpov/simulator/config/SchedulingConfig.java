package com.mikhailkarpov.simulator.config;

import com.mikhailkarpov.simulator.airplane.AirplaneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final AirplaneService airplaneService;

    @Scheduled(cron = "${api.airplane-service.schedule-cron}")
    public void updateAirplanes() {
        log.debug("Updating airplanes...");
        airplaneService.updateAirplanes();
    }

}
