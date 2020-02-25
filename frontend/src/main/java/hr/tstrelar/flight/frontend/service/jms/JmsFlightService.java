package hr.tstrelar.flight.frontend.service.jms;

import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JmsFlightService implements FlightService {
    @Override
    public FlightDto persistFlightData(FlightDto flight) {
        return null;
    }

    @Override
    public FlightDto getSingleFlight(Long id) {
        return null;
    }

    @Override
    public List<FlightDto> searchFlights(FlightDto flight) {
        return null;
    }

    @Override
    public FlightDto updateFlightData(FlightDto flight) {
        return null;
    }
}
