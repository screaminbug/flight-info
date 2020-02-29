package hr.tstrelar.flight.frontend.service.jms;

import hr.tstrelar.flight.frontend.helpers.SyncRequestor;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

import static hr.tstrelar.flight.model.Status.aStatusOf;
import static hr.tstrelar.flight.model.StatusCode.*;

@Service
@Log4j2
public class JmsFlightService implements FlightService {

    private final SyncRequestor<FlightMessage> syncRequestor;

    @Value("${flight.save.queue}")
    private String flightSaveQueue;

    @Value("${flight.get.queue}")
    private String flightGetQueue;

    @Value("${flight.search.queue")
    private String flightSearchQueue;

    @Value("${flight.update.queue")
    private String flightUpdateQueue;

    @Value("${jms.sync.timeout}")
    private Integer timeout;

    @Autowired
    public JmsFlightService(SyncRequestor<FlightMessage> syncRequestor) {
        this.syncRequestor = syncRequestor;
    }

    @Override
    public FlightMessage persistFlightData(FlightDto flight) {
        return publish(flight, flightSaveQueue);
    }

    @Override
    public FlightMessage getSingleFlight(FlightDto flight) {
        return publish(flight, flightGetQueue);
    }

    @Override
    public FlightMessage updateFlightData(FlightDto flight) {
        return publish(flight, flightUpdateQueue);
    }

    @Override
    public FlightMessage searchFlights(FlightDto flight) {
        return publish(flight, flightSearchQueue);
    }


    private FlightMessage publish(FlightDto flight, String queue) {
        UUID uuid = UUID.randomUUID();
        FlightMessage requestMessage = new FlightMessage(uuid, aStatusOf(OK), flight);
        try {
            syncRequestor.setTimeout(timeout);
            syncRequestor.setCorrelationId(uuid.toString());
            Message message = syncRequestor.requestAndWait(requestMessage, queue);
            if (message != null) {
                return message.getBody(FlightMessage.class);
            } else {
                // We don't want to wait forever. If the message is null,
                // it means that the set timeout has been reached.
                // Let backend handle it. The client will get a handle (uuid) which they
                // can use to check if it's done.
                return new FlightMessage(uuid, aStatusOf(ACCEPTED));
            }
        } catch (JMSException e) {
            log.error(e);
        }

        return new FlightMessage(uuid, aStatusOf(SERVER_ERROR));
    }
}
