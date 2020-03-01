package hr.tstrelar.flight.model;

public enum StatusCode {
    OK(200, "OK"),
    SAVED(201, "Entity saved"),
    ACCEPTED(202, "Request accepted, check in later"),
    BAD_REQUEST(400, "Required data has not been received"),
    ALREADY_EXISTS(409, "A flight data with that ID already exists"),
    NOT_FOUND(404, "Entity not found"),
    SERVER_ERROR(500, "Unexpected condition occured");

    String message;
    int code;

    StatusCode(int code, String message) {
        this.message = message;
        this.code = code;
    }

    String getMessage() {
        return message;
    }

    String getCode() {
        return code + "";
    }

}
