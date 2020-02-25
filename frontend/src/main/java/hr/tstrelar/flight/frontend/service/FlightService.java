package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.model.FlightDto;

import java.util.List;

public interface FlightService {
    FlightDto persistFlightData(FlightDto flight);
    FlightDto getSingleFlight(Long id);
    List<FlightDto> searchFlights(FlightDto flight);
    FlightDto updateFlightData(FlightDto flight);
}
