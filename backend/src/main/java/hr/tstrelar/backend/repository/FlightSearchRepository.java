package hr.tstrelar.backend.repository;

import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.flight.model.SearchDto;

import java.util.List;
import java.util.UUID;

public interface FlightSearchRepository {
    List<Flight> searchFlights(SearchDto searchParams);
    boolean existsByFlightId(String flightId);
}
