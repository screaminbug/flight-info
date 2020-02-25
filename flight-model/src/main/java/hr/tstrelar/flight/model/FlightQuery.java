package hr.tstrelar.flight.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FlightQuery {
  private String departure;
  private String arrival;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
          pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private Date departureDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING,
          pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
  private Date arrivalDate;
  private Integer transfers;
  private Integer numberOfPassengers;
  private String company;
}

