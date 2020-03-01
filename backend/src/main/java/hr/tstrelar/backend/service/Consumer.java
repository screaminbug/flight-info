package hr.tstrelar.backend.service;

import hr.tstrelar.backend.domain.Company;
import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.backend.exception.FlightNotFoundException;
import hr.tstrelar.backend.mapper.FlightMapper;
import hr.tstrelar.backend.repository.CompanyRepository;
import hr.tstrelar.backend.repository.FlightRepository;
import hr.tstrelar.flight.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hr.tstrelar.flight.model.Status.aStatusOf;
import static hr.tstrelar.flight.model.StatusCode.*;
import static java.util.Optional.*;

@Component
@Log4j2
public class Consumer {

    private final FlightRepository flightRepository;
    private final CompanyRepository companyRepository;
    private final FlightMapper flightMapper;

    @Autowired
    public Consumer(
            FlightRepository flightRepository,
            CompanyRepository companyRepository,
            FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.companyRepository = companyRepository;
        this.flightMapper = flightMapper;
    }

    @JmsListener(destination = "flight.queue.save.request")
    public ResponseMessage persistFlightData(final Message<RequestMessage> message) {
        RequestMessage requestMessage = message.getPayload();
        FlightDto flightDto = requestMessage.getFlightDto();
        if (flightDto == null) {
            return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(BAD_REQUEST));
        }

        if (flightRepository.existsByFlightId(flightDto.getFlightId())) {
            return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(ALREADY_EXISTS));
        }

        Company company = getOrCreateCompany(flightDto.getCompany());
        Flight flight = flightMapper.flightDtoToFlight(flightDto);
        flight.setCompany(company);
        flight.setId(requestMessage.getMessageId());
        flightRepository.save(flight);
        return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(SAVED), flightMapper.flightToFlightDto(flight));
    }

    @JmsListener(destination = "flight.queue.get.request")
    public ResponseMessage getFlightById(final Message<RequestMessage> message) {
        UUID messageId = message.getPayload().getMessageId();
        return flightRepository.findById(messageId)
                .map($ -> new ResponseMessage(messageId, aStatusOf(OK), flightMapper.flightToFlightDto($)))
                .orElse(new ResponseMessage(messageId, aStatusOf(NOT_FOUND)));
    }

    @JmsListener(destination = "flight.queue.update.request")
    public ResponseMessage updateFlight(final Message<RequestMessage> message) {
        ResponseMessage responseMessage;
        try {
            UUID messageId = message.getPayload().getMessageId();
            Flight existingFlight = flightRepository.findById(messageId).orElseThrow(
                    () -> new FlightNotFoundException(messageId));

            FlightDto newFlightDto = message.getPayload().getFlightDto();

            if (newFlightDto.getCompany() != null) {
                Company company = getOrCreateCompany(newFlightDto.getCompany());
                existingFlight.setCompany(company);
            }
            ofNullable(newFlightDto.getAirportArrival()).ifPresent(existingFlight::setAirportArrival);
            ofNullable(newFlightDto.getAirportDeparture()).ifPresent(existingFlight::setAirportDeparture);
            ofNullable(newFlightDto.getDateDeparture()).ifPresent(existingFlight::setDateDeparture);
            ofNullable(newFlightDto.getDateArrival()).ifPresent(existingFlight::setDateArrival);
            ofNullable(newFlightDto.getFlightId()).ifPresent(existingFlight::setFlightId);
            ofNullable(newFlightDto.getNumberOfPassengers()).ifPresent(existingFlight::setNumberOfPassengers);
            ofNullable(newFlightDto.getNumberOfTransfers()).ifPresent(existingFlight::setNumberOfTransfers);

            flightRepository.save(existingFlight);
            responseMessage = new ResponseMessage(messageId, aStatusOf(OK), flightMapper.flightToFlightDto(existingFlight));
        } catch (FlightNotFoundException e) {
            responseMessage = new ResponseMessage(e.getMessageId(), aStatusOf(NOT_FOUND));
        }
        return responseMessage;
    }

    @JmsListener(destination = "flight.queue.search.request")
    public ResponseMessage searchFlights(final Message<RequestMessage> message) {
        RequestMessage requestMessage = message.getPayload();
        SearchDto searchDto = requestMessage.getSearchDto();

        if (searchDto == null) {
            return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(BAD_REQUEST));
        }
        List<Flight> flights = flightRepository.searchFlights(searchDto);

        return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(OK), flightMapper.flightsToFlightDtos(flights));
    }

    private Company getOrCreateCompany(String name) {
        Company company = companyRepository.findFirstByName(name);
        if (company == null) {
            company = new Company();
            company.setName(name);
            company = companyRepository.save(company);
        }
        return company;
    }
}
