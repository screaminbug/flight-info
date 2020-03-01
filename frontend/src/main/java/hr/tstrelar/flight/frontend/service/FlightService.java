package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.RequestMessage;
import hr.tstrelar.flight.model.ResponseMessage;
import hr.tstrelar.flight.model.SearchDto;

import java.util.UUID;

public interface FlightService {
    ResponseMessage persistFlightData(RequestMessage message);
    ResponseMessage getPreviousResponseOrFlight(RequestMessage message);
    ResponseMessage updateFlightData(RequestMessage message);
    ResponseMessage searchFlights(RequestMessage message);
}
