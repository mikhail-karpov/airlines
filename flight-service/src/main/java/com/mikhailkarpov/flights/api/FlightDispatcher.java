package com.mikhailkarpov.flights.api;

import com.mikhailkarpov.airplane.Airplane;
import com.mikhailkarpov.airplane.AirplaneCommand;
import com.mikhailkarpov.airplane.AirplaneCommandType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("airplane")
public class FlightDispatcher implements Consumer<Airplane> {

    private final FlightService flightService;
    private final StreamBridge streamBridge;

    public FlightDispatcher(FlightService flightService, StreamBridge streamBridge) {
        this.flightService = flightService;
        this.streamBridge = streamBridge;
    }

    @Override
    public void accept(@NonNull Airplane airplane) {
        log.debug("Received: {}", airplane);

        String flightCode = airplane.getFlightCode();
        flightService.findFlightByCode(flightCode).ifPresent(flight -> {
            FlightStatus flightStatus = flight.getStatus();

            switch (airplane.getStatus()) {
                case READY_FOR_TAKE_OFF:
                    if (flightStatus == FlightStatus.SCHEDULED) {
                        sendCommand(flightCode, AirplaneCommandType.TAKE_OFF);
                    }
                    break;
                case IN_FLIGHT:
                    if (flightStatus == FlightStatus.SCHEDULED) {
                        flightService.updateFlight(flightCode, FlightStatus.DEPARTED);
                    }
                    sendCommand(flightCode, AirplaneCommandType.FLY);
                    //TODO airplane position notification
                    break;
                case READY_FOR_LANDING:
                    if (flightStatus == FlightStatus.DEPARTED) {
                        sendCommand(flightCode, AirplaneCommandType.LAND);
                        flightService.updateFlight(flightCode, FlightStatus.ARRIVED);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    @Scheduled(cron = "15 * * * * *")
    public void dispatchFlights() {
        flightService.listFlights(FlightStatus.CREATED).forEach(flight -> {
            log.debug("Scheduling: {}", flight);
            flightService.updateFlight(flight.getCode(), FlightStatus.SCHEDULED);
            sendCommand(flight.getCode(), AirplaneCommandType.TAKE_OFF);
        });
    }

    private void sendCommand(String flightCode, AirplaneCommandType commandType) {
        AirplaneCommand command = new AirplaneCommand(flightCode, commandType);
        log.debug("Sending {}", command);
        streamBridge.send("airplaneCommand-out-0", command);
    }
}
