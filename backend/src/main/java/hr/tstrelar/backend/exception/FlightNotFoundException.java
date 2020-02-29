package hr.tstrelar.backend.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

public class FlightNotFoundException extends RuntimeException {
    @Getter
    private UUID messageId;
    public FlightNotFoundException(UUID messageId) {
       this.messageId = messageId;
    }
}
