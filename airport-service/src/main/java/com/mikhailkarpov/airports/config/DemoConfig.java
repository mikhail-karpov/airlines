package com.mikhailkarpov.airports.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhailkarpov.airports.api.Airport;
import com.mikhailkarpov.airports.api.AirportService;
import com.mikhailkarpov.airports.api.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@Profile("demo")
@EnableConfigurationProperties(DemoProperties.class)
public class DemoConfig {

    @Bean
    ApplicationRunner airportInitializer(ObjectMapper objectMapper,
                                         AirportService airportService,
                                         DemoProperties properties) {
        return args -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(getClass().getResource("/airports.json")).get("airports");
                List<Airport> airports = readAirports(jsonNode);
                Collections.shuffle(airports);
                airports.stream().limit(properties.getAirportsTotal()).forEach(airportService::saveAirport);

            } catch (Exception e) {
                log.warn("Failed to read airports", e);
            }
        };
    }

    private static List<Airport> readAirports(JsonNode arrayNode) {
        List<Airport> airports = new ArrayList<>();

        for (JsonNode node : arrayNode) {
            String code = node.get("code").asText();
            String city = node.get("city").asText();
            double longitude = Double.parseDouble(node.get("lon").asText());
            double latitude = Double.parseDouble(node.get("lat").asText());
            int directFlights = Integer.parseInt(node.get("direct_flights").asText());

            if (directFlights > 75) {
                Airport airport = new Airport(code, city, new Location(longitude, latitude));
                airports.add(airport);
            }
        }
        return airports;
    }
}
