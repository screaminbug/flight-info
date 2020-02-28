package hr.tstrelar.flight.frontend.service;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;

@FunctionalInterface
public interface PerformServiceCall<T extends FlightDto, R extends FlightResponse> {
    R apply(T t) throws FlightServiceException;
}
