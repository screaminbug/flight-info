package hr.tstrelar.backend.service;

import hr.tstrelar.backend.domain.Company;
import hr.tstrelar.backend.domain.Flight;
import hr.tstrelar.backend.mapper.FlightMapper;
import hr.tstrelar.backend.repository.CompanyRepository;
import hr.tstrelar.backend.repository.FlightRepository;
import hr.tstrelar.flight.model.FlightDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

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

    @JmsListener(destination = "flight.save.queue.request")
    public FlightDto receiveMessage(final Message<FlightDto> message) throws InterruptedException {
        FlightDto flightDto = message.getPayload();
        Company company = companyRepository.findFirstByName(flightDto.getCompany());
        if (company == null) {
            company = new Company();
            company.setName(flightDto.getCompany());
            company = companyRepository.save(company);
        }
        Flight flight = flightMapper.flightDtoToFlight(message.getPayload());
        flight.setCompany(company);
        flight = flightRepository.save(flight);
        return flightMapper.flightToFlightDto(flight);
    }
}
