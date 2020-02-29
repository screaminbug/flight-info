package hr.tstrelar.backend.service;

import hr.tstrelar.backend.domain.Company;
import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.backend.mapper.FlightMapper;
import hr.tstrelar.backend.repository.CompanyRepository;
import hr.tstrelar.backend.repository.FlightRepository;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.RequestMessage;
import hr.tstrelar.flight.model.ResponseMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static hr.tstrelar.flight.model.Status.aStatusOf;
import static hr.tstrelar.flight.model.StatusCode.*;

@Component
@Log4j2
public class Consumer {

    private final FlightRepository flightRepository;
    private final CompanyRepository companyRepository;
    private final FlightMapper flightMapper;

    private final String DEST_SUFFIX = ".request";

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
        //TODO: Implement flight id and return error if that id already exists
        Company company = companyRepository.findFirstByName(flightDto.getCompany());
        if (company == null) {
            company = new Company();
            company.setName(flightDto.getCompany());
            company = companyRepository.save(company);
        }
        Flight flight = flightMapper.flightDtoToFlight(flightDto);
        flight.setCompany(company);
        flight.setId(requestMessage.getMessageId());
        flightRepository.save(flight);
        return new ResponseMessage(requestMessage.getMessageId(), aStatusOf(SAVED), flightMapper.flightToFlightDto(flight));
    }

    @JmsListener(destination = "flight.queue.get.request")
    public ResponseMessage searchFlights(final Message<RequestMessage> message) {
        UUID messageId = message.getPayload().getMessageId();
        return flightRepository.findById(messageId)
                .map($ -> new ResponseMessage(messageId, aStatusOf(OK), flightMapper.flightToFlightDto($)))
                .orElse(new ResponseMessage(messageId, aStatusOf(NOT_FOUND)));
    }
}
