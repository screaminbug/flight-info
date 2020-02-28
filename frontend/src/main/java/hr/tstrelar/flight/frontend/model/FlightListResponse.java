package hr.tstrelar.flight.frontend.model;

import hr.tstrelar.flight.model.FlightDto;
import lombok.Data;

import java.util.List;

@Data
public class FlightListResponse extends FlightResponse{
    private List<FlightDto> flightDtos;
}
