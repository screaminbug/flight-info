package hr.tstrelar.flight.frontend.service.jms;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.helpers.SyncRequestor;
import hr.tstrelar.flight.frontend.model.FlightListResponse;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.UUID;

@Service
public class JmsFlightService implements FlightService {

    private final SyncRequestor<FlightDto> syncRequestor;

    @Value("${flight.save.queue}")
    private String flightSaveQueue;

    @Value("${jms.sync.timeout}")
    private Integer timeout;

    @Autowired
    public JmsFlightService(SyncRequestor<FlightDto> syncRequestor) {
        this.syncRequestor = syncRequestor;
    }

    @Override
    public FlightResponse persistFlightData(FlightDto flight) throws FlightServiceException {
        FlightResponse response = new FlightResponse();
        response.setRequestId(UUID.randomUUID().toString());
        try {
            syncRequestor.setTimeout(timeout);
            syncRequestor.setCorrelationId(response.getRequestId());
            Message message = syncRequestor.requestAndWait(flight, flightSaveQueue);
            if (message != null) {
                // if we timed out, message will bi null, but request id will still be set
                // we call this near real time processing
                response.setFlightDto(message.getBody(FlightDto.class));
            }
        } catch (JMSException e) {
            throw new FlightServiceException(e);
        }

        return response;

    }

    @Override
    public FlightResponse getSingleFlight(Long id) {
        return null;
    }

    @Override
    public FlightResponse updateFlightData(FlightDto flight) {
        return null;
    }

    @Override
    public FlightListResponse searchFlights(FlightDto flight) {
        return null;
    }
}
