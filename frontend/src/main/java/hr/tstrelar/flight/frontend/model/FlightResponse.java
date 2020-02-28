package hr.tstrelar.flight.frontend.model;

import lombok.Data;

@Data
public class FlightResponse {
    private String requestId;
    private String errorMessage;
}
