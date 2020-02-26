package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.frontend.restapi.FlightApi;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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
    public DeferredResult<ResponseEntity<FlightResponse>> addFlight(@Valid FlightDto body) {
        DeferredResult<ResponseEntity<FlightResponse>> output = new DeferredResult<>(timeout + 100);
        ForkJoinPool.commonPool().submit(
            () -> {
                try {
                    output.setResult(ResponseEntity.ok(jmsFlightService.persistFlightData(body)));
                } catch (FlightServiceException e) {
                    FlightResponse response = new FlightResponse();
                    assert(e.getCause() != null);
                    response.setErrorMessage(e.getCause().getMessage());;
                }
            }
        );
        final FlightResponse timeoutResponse = new FlightResponse();
        timeoutResponse.setErrorMessage("Waited longer than expected. Try Again...");
        output.onTimeout(() -> output.setResult(ResponseEntity.accepted().body(timeoutResponse)));

        return output;
    }

    @Override
    public ResponseEntity<List<FlightDto>> findFlight(
            @NotNull @Valid List<String> departureDate,
            @NotNull @Valid List<String> arrivalDate,
            @Valid List<String> departure,
            @Valid List<String> arrival,
            @Valid List<Integer> transfers,
            @Valid List<Integer> numberOfPassengers,
            @Valid List<String> company) {
        return null;
    }

    @Override
    public ResponseEntity<FlightDto> getFlightById(Long flightId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateFlight(@Valid FlightDto body) {
        return null;
    }
}
