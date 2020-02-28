package hr.tstrelar.flight.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class FlightMessage implements Serializable {
    private UUID messageId;
    private FlightDto flightDto;
}
