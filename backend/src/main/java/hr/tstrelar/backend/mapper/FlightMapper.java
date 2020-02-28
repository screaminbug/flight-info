package hr.tstrelar.backend.mapper;

import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.flight.model.FlightDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mappings({
        @Mapping(target = "company", ignore = true)
    })
    Flight flightDtoToFlight(FlightDto flightDto);

    @Mappings({
        @Mapping(target = "company", ignore = true)
    })
    @Mapping(ignore = true, target = "company")
    FlightDto flightToFlightDto(Flight flight);
}
