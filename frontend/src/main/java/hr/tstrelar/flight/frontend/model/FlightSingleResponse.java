package hr.tstrelar.flight.frontend.model;

import hr.tstrelar.flight.model.FlightDto;
import lombok.Data;

@Data
public class FlightSingleResponse extends FlightResponse {
    private FlightDto flightDto;
}
