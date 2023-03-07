package com.mikhailkarpov.airports.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    @DisplayName("Should lis airports")
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

}