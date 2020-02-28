package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.model.FlightListResponse;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.frontend.model.FlightSingleResponse;
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
    public DeferredResult<ResponseEntity<FlightSingleResponse>> addFlight(@Valid FlightDto body) {
        // TODO: validate request
        return DeferredFlightServiceResult.Builder.create(timeout + 200, FlightSingleResponse.class)
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
    public DeferredResult<ResponseEntity<FlightListResponse>> findFlight(
            @NotNull @Valid Date departureDate,
            @NotNull @Valid Date arrivalDate,
            @Valid String departure,
            @Valid String arrival,
            @Valid Integer transfers,
            @Valid Integer numberOfPassengers,
            @Valid String company) {
        // TODO: add validation
        FlightDto flightDto = new FlightDto(
                departure,
                arrival,
                departureDate,
                transfers,
                numberOfPassengers,
                company,
                null
        );

        return DeferredFlightServiceResult.Builder.create(timeout, FlightListResponse.class)
                .withRequest(flightDto).build()
                .callWith(jmsFlightService::searchFlights);
    }

    @Override
    public ResponseEntity<FlightSingleResponse> getFlightById(Long flightId) {
        return null;
    }

    @Override
    public ResponseEntity<FlightSingleResponse> updateFlight(@Valid FlightDto body) {
        return null;
    }


}
