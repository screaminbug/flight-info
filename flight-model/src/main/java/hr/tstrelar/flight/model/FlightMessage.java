package hr.tstrelar.flight.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class FlightMessage implements Serializable {
    private UUID messageId;
    private Status status;
    @JsonInclude(NON_NULL)
    private FlightDto flight;
    @JsonInclude(NON_NULL)
    private List<FlightDto> flights;

    public FlightMessage(UUID messageId, Status status) {
        this.messageId = messageId;
        this.status = status;
    }

    public FlightMessage(UUID messageId, Status status, FlightDto flight) {
        this(messageId, status);
        this.flight = flight;
    }

    public FlightMessage(UUID messageId, Status status, List<FlightDto> flights) {
        this(messageId, status);
        this.flights = flights;
    }
}
