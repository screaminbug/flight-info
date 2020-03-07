package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.frontend.service.jms.DeferredFlightServiceResult;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.ResponseMessage;
import hr.tstrelar.flight.model.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public DeferredResult<ResponseEntity<ResponseMessage>> addFlight(
            @RequestBody FlightDto flightDto) {
        validate(flightDto);
        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + timeout)
                .withRequest(flightDto).build()
                .callWith(jmsFlightService::persistFlightData);
    }

    @PostMapping(path = "flight/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public DeferredResult<ResponseEntity<ResponseMessage>> updateFlight(
            @RequestBody FlightDto flightDto,
            @PathVariable String id) {
        validate(flightDto, id);
        return DeferredFlightServiceResult.Builder.create()
                .withTimeout(timeout + timeout)
                .withRequest(flightDto, UUID.fromString(id))
                .build()
                .callWith(jmsFlightService::updateFlightData);
    }

    @GetMapping(path = "flight/find", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public DeferredResult<ResponseEntity<ResponseMessage>> findFlight(
            @RequestParam("date-dpt-from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    Date dateDepartureFrom,
            @RequestParam("date-dpt-to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    Date dateDepartureTo,
            @RequestParam("date-arr-from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    Date dateArrivalFrom,
            @RequestParam("date-arr-to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    Date dateArrivalTo,
            @RequestParam("departure-iata")
            @Nullable
                    List<String> departureAirports,
            @RequestParam("arrival-iata")
            @Nullable
                    List<String> arrivalAirports,
            @RequestParam("passenger-count")
            @Nullable
                    List<Integer> numberOfPassengers,
            @RequestParam("transfer-count")
            @Nullable
                    List<Integer> transfers,
            @RequestParam("company")
            @Nullable
                    List<String> companies,
            @RequestParam("flight-id")
            @Nullable
                    List<String> flightIds) {
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
                .withTimeout(timeout + timeout)
                .withRequest(searchDto)
                .build()
                .callWith(jmsFlightService::searchFlights);
    }

    @GetMapping(value = "resource/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public DeferredResult<ResponseEntity<ResponseMessage>> getResultByMessageId(
            @PathVariable("id") String messageId) {
        return DeferredFlightServiceResult.Builder
                .create()
                .withTimeout(timeout+timeout)
                .withRequest(UUID.fromString(messageId))
                .build().callWith(jmsFlightService::getPreviousResponseOrFlight);
    }

    private void validate(FlightDto flightDto) {
        if (
                flightDto.getCompany() == null
                || flightDto.getAirportArrival() == null
                || flightDto.getAirportDeparture() == null
                || flightDto.getDateArrival() == null
                || flightDto.getDateDeparture() == null
                || flightDto.getFlightId() == null
                || flightDto.getNumberOfPassengers() == null
                || flightDto.getNumberOfTransfers() == null
        ) { throw new IllegalArgumentException(); }
    }

    private void validate(FlightDto flightDto, String id) {
        validate(flightDto);
        if (id == null) { throw new IllegalArgumentException(); }
    }
}
