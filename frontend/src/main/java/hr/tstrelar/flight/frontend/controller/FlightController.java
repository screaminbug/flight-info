package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.frontend.service.jms.DeferredFlightServiceResult;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.ResponseMessage;
import hr.tstrelar.flight.model.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "v1")
public class FlightController  {

    @Value("${jms.sync.timeout}")
    private Long timeout;

    private final FlightService jmsFlightService;

    @Autowired
    public FlightController(FlightService jmsFlightService) {
        this.jmsFlightService = jmsFlightService;
    }

    @PostMapping(path = "flight", produces = "application/json", consumes = "application/json")
    public DeferredResult<ResponseEntity<ResponseMessage>> addFlight(
            @RequestBody FlightDto flightDto) {
        // TODO: validate request
        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + 100)
                .withRequest(flightDto).build()
                .callWith(jmsFlightService::persistFlightData);
    }

    @PostMapping(path = "flight/{id}", produces = "application/json", consumes = "application/json")
    public DeferredResult<ResponseEntity<ResponseMessage>> updateFlight(
            @RequestBody FlightDto body,
            @PathVariable String id) {
        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + 100)
                .withRequest(body, UUID.fromString(id))
                .build()
                .callWith(jmsFlightService::updateFlightData);
    }

    @GetMapping(path = "flight/find", produces = "application/json")
    public DeferredResult<ResponseEntity<ResponseMessage>> findFlight(
            @RequestParam("date-dpt-from") @Nullable Date dateDepartureFrom,
            @RequestParam("date-dpt-to") @Nullable Date dateDepartureTo,
            @RequestParam("date-arr-from") @Nullable Date dateArrivalFrom,
            @RequestParam("date-arr-to") @Nullable Date dateArrivalTo,
            @RequestParam("departure-iata") @Nullable List<String> departureAirports,
            @RequestParam("arrival-iata") @Nullable List<String> arrivalAirports,
            @RequestParam("passanger-count") @Nullable List<Integer> numberOfPassengers,
            @RequestParam("trasfers-count") @Nullable List<Integer> transfers,
            @RequestParam("company") @Nullable List<String> companies,
            @RequestParam("flight-id") @Nullable List<String> flightIds) {
        // TODO: add validation
        SearchDto searchDto = new SearchDto(
                dateDepartureFrom,
                dateDepartureTo,
                dateArrivalFrom,
                dateArrivalTo,
                departureAirports,
                arrivalAirports,
                numberOfPassengers,
                transfers,
                companies,
                flightIds
        );

        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + 100)
                .withRequest(searchDto)
                .build()
                .callWith(jmsFlightService::searchFlights);
    }

    @GetMapping(value = "resource/{id}", produces = "application/json")
    public DeferredResult<ResponseEntity<ResponseMessage>> getResultByMessageId(
            @PathVariable("id") String messageId) {
        return DeferredFlightServiceResult.Builder
                .create()
                .withTimeout(timeout+100)
                .withRequest(UUID.fromString(messageId))
                .build().callWith(jmsFlightService::getPreviousResponseOrFlight);
    }
}
