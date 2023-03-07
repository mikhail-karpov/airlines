package com.mikhailkarpov.airports.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AirportController.class)
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Test
    @DisplayName("Should list airports")
    void listAirports() throws Exception {

        Mockito.when(airportService.listAirports()).thenReturn(List.of(
                new Airport("code 1", "City 1", new Location(2.0, 3.5)),
                new Airport("code 2", "City 2", new Location(2.5, -3.5))
        ));

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].code").value("code 2"))
                .andExpect(jsonPath("$[1].city").value("City 2"))
                .andExpect(jsonPath("$[1].location.x").value(2.5))
                .andExpect(jsonPath("$[1].location.y").value(-3.5));
    }

    @Test
    @DisplayName("Should get airport by code")
    void getAirport() throws Exception {

        Mockito.when(airportService.findAirportByCode("ABC")).thenReturn(Optional.of(
                new Airport("ABC", "City", new Location(2.0, 3.5))
        ));

        mockMvc.perform(get("/api/v1/airports/ABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.city").value("City"))
                .andExpect(jsonPath("$.location.x").value(2.0))
                .andExpect(jsonPath("$.location.y").value(3.5));
    }

    @Test
    @DisplayName("Should return 404 when airport not found by code")
    void getAirportNotFound() throws Exception {

        Mockito.when(airportService.findAirportByCode("ABC")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/airports/ABC"))
                .andExpect(status().isNotFound());
    }
}