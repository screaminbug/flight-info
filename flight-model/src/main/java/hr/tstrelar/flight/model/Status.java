package hr.tstrelar.flight.model;

import lombok.Getter;

import java.io.Serializable;

public class Status implements Serializable {
    @Getter
    private final String statusCode;
    @Getter
    private final String statusMessage;

    private Status(StatusCode statusCode) {
        this.statusCode = statusCode.getCode();
        statusMessage = statusCode.getMessage();
    }

    public static Status aStatusOf(StatusCode statusCode) {
        return new Status(statusCode);
    }
}
