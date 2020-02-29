package hr.tstrelar.flight.frontend.helpers;

import hr.tstrelar.flight.model.Status;

import static hr.tstrelar.flight.model.Status.aStatusOf;
import static hr.tstrelar.flight.model.StatusCode.SERVER_ERROR;

public class Constants {
    public static final String REQUEST_SUFFIX = ".request";
    public static final String RESPONSE_SUFFIX = ".response";
    public static final int DEFAULT_RECEIVE_TIMEOUT = 600;
    public static final Status ERROR_STATUS = aStatusOf(SERVER_ERROR);
}
