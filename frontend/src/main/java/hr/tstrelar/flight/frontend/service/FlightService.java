package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.model.FlightListResponse;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.frontend.model.FlightSingleResponse;
import hr.tstrelar.flight.model.FlightDto;

public interface FlightService {
    FlightSingleResponse persistFlightData(FlightDto flight) throws FlightServiceException;
    FlightSingleResponse getSingleFlight(FlightDto id);
    FlightSingleResponse updateFlightData(FlightDto flight);
    FlightListResponse searchFlights(FlightDto flight);
}
