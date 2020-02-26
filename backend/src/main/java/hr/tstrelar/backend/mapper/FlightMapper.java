package hr.tstrelar.backend.mapper;

import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.flight.model.FlightDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mapping(ignore = true, target = "company", source = "company")
    Flight flightDtoToFlight(FlightDto flightDto);
    @Mapping(ignore = true, target = "company", source = "company")
    FlightDto flightToFlightDto(Flight flight);
}
