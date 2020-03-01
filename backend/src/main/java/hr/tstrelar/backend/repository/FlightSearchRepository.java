package hr.tstrelar.backend.repository;

import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.flight.model.SearchDto;

import java.util.List;

public interface FlightSearchRepository {
    List<Flight> searchFlights(SearchDto searchParams);
}
