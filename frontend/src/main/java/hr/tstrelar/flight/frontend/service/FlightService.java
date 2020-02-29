package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;

public interface FlightService {
    FlightMessage persistFlightData(FlightDto flight);
    FlightMessage getSingleFlight(FlightDto id);
    FlightMessage updateFlightData(FlightDto flight);
    FlightMessage searchFlights(FlightDto flight);
}
