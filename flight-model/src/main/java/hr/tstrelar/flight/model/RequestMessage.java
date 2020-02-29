package hr.tstrelar.flight.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

public class RequestMessage implements Serializable {
    @Getter
    @Setter
    private UUID messageId;
    @Getter
    private FlightDto flightDto;
    @Getter
    private SearchDto searchDto;


    public RequestMessage(UUID messageId) {
        this.messageId = messageId;
    }

    public RequestMessage(FlightDto flightDto) {
        this.flightDto = flightDto;
    }

    public RequestMessage(SearchDto searchDto) {
        this.searchDto = searchDto;
    }

}
