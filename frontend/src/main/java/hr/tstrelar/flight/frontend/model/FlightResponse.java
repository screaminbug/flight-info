package hr.tstrelar.flight.frontend.model;

import hr.tstrelar.flight.model.FlightDto;
import lombok.Data;

@Data
public class FlightResponse {
    private String requestId;
    private String errorMessage;
    private FlightDto flightDto;
}
