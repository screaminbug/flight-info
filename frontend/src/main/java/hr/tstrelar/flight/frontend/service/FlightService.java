package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.model.FlightListResponse;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.model.FlightDto;

import javax.jms.JMSException;
import java.util.List;

public interface FlightService {
    FlightResponse persistFlightData(FlightDto flight) throws FlightServiceException;
    FlightResponse getSingleFlight(Long id);
    FlightResponse updateFlightData(FlightDto flight);
    FlightListResponse searchFlights(FlightDto flight);
}
