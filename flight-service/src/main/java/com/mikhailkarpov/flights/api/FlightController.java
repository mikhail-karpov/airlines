package com.mikhailkarpov.flights.api;

import com.mikhailkarpov.flights.api.exceptions.FlightNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public Flight createFlight(@Valid @RequestBody CreateFlightRequest request) {

        return flightService.createFlight(request);
    }

    @GetMapping
    public List<Flight> listFlights() {

        return flightService.listFlights();
    }

    @GetMapping("/{code}")
    public Flight getFlightByCode(@PathVariable String code) {

        return flightService.findFlightByCode(code).orElseThrow(
                () -> new FlightNotFoundException(code)
        );
    }
}
