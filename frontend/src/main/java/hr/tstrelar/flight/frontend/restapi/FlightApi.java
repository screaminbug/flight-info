package hr.tstrelar.flight.frontend.restapi;

import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Api(value = "flight")
public interface FlightApi {

    @ApiOperation(value = "Add new flight to flight info", nickname = "addFlight", notes = "", authorizations = {
            @Authorization(value = "flightinfo_auth", scopes = {
                @AuthorizationScope(scope = "write:flights", description = "modify flights in your account"),
                @AuthorizationScope(scope = "read:flights", description = "read flights")
            })
    }, tags={ "flight", })
    @ApiResponses(value = { 
        @ApiResponse(code = 405, message = "Invalid input"),
        @ApiResponse(code = 202, message = "FlightResponse object with request id (for future use)"),
        @ApiResponse(code = 201, message = "FlightResponse object with flightId populated")})
    @PostMapping(value = "/flight",
        produces = { "application/json" }, 
        consumes = { "application/json" })
    DeferredResult<ResponseEntity<FlightMessage>> addFlight(@ApiParam(value = "A flight information that needs to be added to the repo", required = true) @Valid @RequestBody FlightDto body);


//    @ApiOperation(value = "Finds flights by departure and arrival locations, departure and arrival dates, number of transfers in outbound and inbound directions, number of passengers, currency, price and operating company", nickname = "findFlight", notes = "Multiple status values can be provided with comma separated strings", response = FlightDto.class, responseContainer = "List", authorizations = {
//        @Authorization(value = "flightinfo_auth", scopes = {
//            @AuthorizationScope(scope = "write:flights", description = "modify flights in your account"),
//            @AuthorizationScope(scope = "read:flights", description = "read your pets")
//            })
//    }, tags={ "flight", })
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "successful operation", response = FlightDto.class, responseContainer = "List"),
//        @ApiResponse(code = 400, message = "Invalid status value") })
//    @GetMapping(value = "/flight/find", produces = { "application/json" })
//    DeferredResult<ResponseEntity<List<FlightDto>>> findFlight(@NotNull @ApiParam(value = "The date and time of the departure", required = true) @Valid @RequestParam(value = "departure-date", required = true) List<String> departureDate, @NotNull @ApiParam(value = "The date and time of the arrival", required = true) @Valid @RequestParam(value = "arrival-date", required = true) List<String> arrivalDate, @ApiParam(value = "Departure location that need to be considered for filter (IATA codes)", allowableValues = "AAL, AES, AAR, HHN, FRA, ZAG, AMS") @Valid @RequestParam(value = "departure", required = false) List<String> departure, @ApiParam(value = "Arrival location that need to be considered for filter (IATA codes)", allowableValues = "AAL, AES, AAR, HHN, FRA, ZAG, AMS") @Valid @RequestParam(value = "Arrival", required = false) List<String> arrival, @ApiParam(value = "The number of transfers") @Valid @RequestParam(value = "transfers", required = false) List<Integer> transfers, @ApiParam(value = "The number of passengers") @Valid @RequestParam(value = "number-of-passengers", required = false) List<Integer> numberOfPassengers, @ApiParam(value = "The company that is providing the flight") @Valid @RequestParam(value = "company", required = false) List<String> company);

    @ApiOperation(value = "Finds flights by departure and arrival locations, departure and arrival dates, number of transfers in outbound and inbound directions, number of passengers, currency, price and operating company", nickname = "findFlight", notes = "Multiple status values can be provided with comma separated strings", response = FlightDto.class, responseContainer = "List", authorizations = {
            @Authorization(value = "flightinfo_auth", scopes = {
                    @AuthorizationScope(scope = "write:flights", description = "modify flights in your account"),
                    @AuthorizationScope(scope = "read:flights", description = "read your pets")
            })
    }, tags={ "flight", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = FlightDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid status value") })
    @GetMapping(value = "/flight/find", produces = { "application/json" })
    DeferredResult<ResponseEntity<FlightMessage>> findFlight(
            @NotNull @ApiParam(value = "The date and time of the departure", required = true)
            @Valid @RequestParam(value = "departure-date", required = true)
                    Date departureDate,
            @NotNull @ApiParam(value = "The date and time of the arrival", required = true)
            @Valid @RequestParam(value = "arrival-date", required = true)
                    Date arrivalDate,
            @ApiParam(value = "Departure location that need to be considered for filter (IATA codes)", allowableValues = "AAL, AES, AAR, HHN, FRA, ZAG, AMS")
            @Valid @RequestParam(value = "departure", required = false)
                    String departure,
            @ApiParam(value = "Arrival location that need to be considered for filter (IATA codes)", allowableValues = "AAL, AES, AAR, HHN, FRA, ZAG, AMS")
            @Valid @RequestParam(value = "Arrival", required = false)
                    String arrival,
            @ApiParam(value = "The number of transfers")
            @Valid @RequestParam(value = "transfers", required = false)
                    Integer transfers,
            @ApiParam(value = "The number of passengers")
            @Valid @RequestParam(value = "number-of-passengers", required = false)
                    Integer numberOfPassengers,
            @ApiParam(value = "The company that is providing the flight")
            @Valid @RequestParam(value = "company", required = false)
                    String company);


    @ApiOperation(value = "Find flight by ID", nickname = "getFlightById", notes = "Returns a single flight", response = FlightDto.class, authorizations = {
        @Authorization(value = "flightinfo_auth", scopes = {
            @AuthorizationScope(scope = "write:flights", description = "modify flights in your account"),
            @AuthorizationScope(scope = "read:flights", description = "read your pets")
            })
    }, tags={ "flight", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response = FlightDto.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found") })
    @GetMapping(value = "/flight/{flightId}",
        produces = { "application/json" })
    DeferredResult<ResponseEntity<FlightMessage>> getFlightById(@ApiParam(value = "ID of flight to return", required = true) @PathVariable("flightId") String flightId);

    @ApiOperation(value = "Update an existing flight", nickname = "updateFlight", notes = "", authorizations = {
        @Authorization(value = "flightinfo_auth", scopes = {
            @AuthorizationScope(scope = "write:flights", description = "modify flights in your account"),
            @AuthorizationScope(scope = "read:flights", description = "read your pets")
            })
    }, tags={ "flight", })
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Flight not found"),
        @ApiResponse(code = 405, message = "Validation exception") })
    @PutMapping(value = "/flight",
        produces = { "application/json" },
        consumes = { "application/json" })
    ResponseEntity<FlightMessage> updateFlight(@ApiParam(value = "Flight object that needs to be updated", required = true) @Valid @RequestBody FlightDto body);

}
