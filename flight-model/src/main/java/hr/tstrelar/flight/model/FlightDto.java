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
  private UUID id;
  private String departureAirport;
  private String arrivalAirport;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Date dateDeparture;
  private Integer numberOfTransfers;
  private Integer numberOfPassengers;
  private String company;
  private Long flightId;
}

