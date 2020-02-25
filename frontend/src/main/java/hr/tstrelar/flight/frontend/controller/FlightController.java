package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.api.FlightApi;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "v1")
public class FlightController implements FlightApi {

    private final FlightService jmsFlightService;

    @Autowired
    public FlightController(FlightService jmsFlightService) {
        this.jmsFlightService = jmsFlightService;
    }


    @Override
    public ResponseEntity<FlightDto> addFlight(@Valid FlightDto body) {
        return null;
    }

    @Override
    public ResponseEntity<List<FlightDto>> findFlight(
            @NotNull @Valid List<String> departureDate,
            @NotNull @Valid List<String> arrivalDate,
            @Valid List<String> departure,
            @Valid List<String> arrival,
            @Valid List<Integer> transfers,
            @Valid List<Integer> numberOfPassengers,
            @Valid List<String> company) {
        return null;
    }

    @Override
    public ResponseEntity<FlightDto> getFlightById(Long flightId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateFlight(@Valid FlightDto body) {
        return null;
    }
}
