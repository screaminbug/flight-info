package hr.tstrelar.backend.mapper;

import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.flight.model.FlightDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mappings({
        @Mapping(target = "company", ignore = true)
    })
    Flight flightDtoToFlight(FlightDto flightDto);

    @Mappings({
        @Mapping(target = "company", source = "company.name")
    })
    FlightDto flightToFlightDto(Flight flight);

    List<FlightDto> flightsToFlightDtos(List<Flight> flights);
}
