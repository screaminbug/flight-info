package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.restapi.FlightApi;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path = "v1")
public class FlightController implements FlightApi {

    @Value("${jms.sync.timeout}")
    private Long timeout;

    private final FlightService jmsFlightService;

    @Autowired
    public FlightController(FlightService jmsFlightService) {
        this.jmsFlightService = jmsFlightService;
    }

    @Override
    public DeferredResult<ResponseEntity<FlightMessage>> addFlight(@Valid FlightDto body) {
        // TODO: validate request
        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + 100)
                .withRequest(body).build()
                .callWith(jmsFlightService::persistFlightData);
    }

//    @Override
//    public DeferredResult<ResponseEntity<List<FlightMessage>>> findFlight(
//            @NotNull @Valid List<String> departureDate,
//            @NotNull @Valid List<String> arrivalDate,
//            @Valid List<String> departure,
//            @Valid List<String> arrival,
//            @Valid List<Integer> transfers,
//            @Valid List<Integer> numberOfPassengers,
//            @Valid List<String> company) {
//        return null;
//    }

    @Override
    public DeferredResult<ResponseEntity<FlightMessage>> findFlight(
            @NotNull @Valid Date departureDate,
            @NotNull @Valid Date arrivalDate,
            @Valid String departure,
            @Valid String arrival,
            @Valid Integer transfers,
            @Valid Integer numberOfPassengers,
            @Valid String company) {
        // TODO: add validation
        FlightDto flightDto = new FlightDto(
                null,
                departure,
                arrival,
                departureDate,
                transfers,
                numberOfPassengers,
                company,
                null
        );

        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + 100)
                .withRequest(flightDto)
                .build()
                .callWith(jmsFlightService::searchFlights);
    }

    @Override
    public DeferredResult<ResponseEntity<FlightMessage>> getFlightById(String flightId) {
        FlightDto flightDto = new FlightDto(UUID.fromString(flightId), null, null, null, null, null, null, null);
        return DeferredFlightServiceResult.Builder.create().withTimeout(timeout+100).withRequest(flightDto)
                .build().callWith(jmsFlightService::getSingleFlight);
    }

    @Override
    public ResponseEntity<FlightMessage> updateFlight(@Valid FlightDto body) {
        return null;
    }


}
