package hr.tstrelar.flight.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto implements Serializable {
    private String company;
    private UUID id;
    private String airportDeparture;
    private String airportArrival;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateArrival;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateDeparture;
    private Integer numberOfTransfers;
    private Integer numberOfPassengers;
    private String flightId;
}

