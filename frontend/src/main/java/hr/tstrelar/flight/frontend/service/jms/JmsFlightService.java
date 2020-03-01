package hr.tstrelar.flight.frontend.service.jms;

import hr.tstrelar.flight.frontend.helpers.CorrelationSelector;
import hr.tstrelar.flight.frontend.helpers.SyncRequestor;
import hr.tstrelar.flight.frontend.service.FlightService;
import hr.tstrelar.flight.model.RequestMessage;
import hr.tstrelar.flight.model.ResponseMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Optional;
import java.util.UUID;

import static hr.tstrelar.flight.frontend.helpers.Constants.RESPONSE_SUFFIX;
import static hr.tstrelar.flight.model.Status.aStatusOf;
import static hr.tstrelar.flight.model.StatusCode.*;
import static org.springframework.jms.support.destination.JmsDestinationAccessor.RECEIVE_TIMEOUT_NO_WAIT;

@Service
@Log4j2
public class JmsFlightService implements FlightService {

    private final SyncRequestor<RequestMessage> syncRequestor;
    private final JmsTemplate jmsTemplate;

    @Value("${flight.queue.save}")
    private String requestSaveQueue;

    @Value("${flight.queue.get}")
    private String requestGetQueue;

    @Value("${flight.queue.search}")
    private String requestSearchQueue;

    @Value("${flight.queue.update}")
    private String requestUpdateQueue;

    @Value("${jms.sync.timeout}")
    private Integer timeout;


    @Autowired
    public JmsFlightService(SyncRequestor<RequestMessage> syncRequestor, JmsTemplate jmsTemplate) {
        this.syncRequestor = syncRequestor;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public ResponseMessage persistFlightData(RequestMessage message) {
        UUID messageId = UUID.randomUUID();
        message.setMessageId(messageId);
        return push(message, requestSaveQueue);
    }

    @Override
    public ResponseMessage getPreviousResponseOrFlight(RequestMessage message) {
        checkMessageIdExists(message, "get flight");

        // see if we have a message waiting for us in any of the queues
        ResponseMessage previousResult = checkQueues(message.getMessageId());
        if (previousResult != null) { return previousResult; }

        // if not, push the new request to the queue
        return push(message, requestGetQueue);
    }

    @Override
    public ResponseMessage updateFlightData(RequestMessage message) {
        checkMessageIdExists(message, "update flight");
        return push(message, requestUpdateQueue);
    }

    @Override
    public ResponseMessage searchFlights(RequestMessage message) {
        message.setMessageId(UUID.randomUUID());
        return push(message, requestSearchQueue);
    }

    private ResponseMessage checkQueues(UUID messageId) {
        // we don't want to wait, pop the message only if it's already in the queue
        jmsTemplate.setReceiveTimeout(RECEIVE_TIMEOUT_NO_WAIT);
        return popMessage(messageId, requestGetQueue)
                .orElse(popMessage(messageId, requestSaveQueue)
                .orElse(popMessage(messageId, requestSearchQueue)
                .orElse(popMessage(messageId, requestUpdateQueue)
                .orElse(null))));
    }

    private Optional<ResponseMessage> popMessage(UUID messageId, String queue) {
        try {
            Message previousResult = jmsTemplate.receiveSelected(
                    queue + RESPONSE_SUFFIX,
                    CorrelationSelector.select(messageId.toString()));
            if (previousResult != null) { return Optional.ofNullable(previousResult.getBody(ResponseMessage.class)); }
        } catch (JMSException e) {
            log.error("An exception occurred while checking queue '{}' for message ID '{}', exception was: {}",
                    queue,
                    messageId,
                    e);
        }
        return Optional.empty();
    }

    private void checkMessageIdExists(RequestMessage message, String info) {
        if (message.getMessageId() == null) {
            log.error("Calling {}, but no ID is set. Message was: {}", info, message);
            throw new IllegalArgumentException("Id for message must be set when searching or updating record");
        }
    }

    private ResponseMessage push(RequestMessage requestMessage, String queue) {
        UUID messageId = requestMessage.getMessageId();
        try {
            syncRequestor.setTimeout(timeout);
            syncRequestor.setCorrelationId(messageId.toString());
            Message message = syncRequestor.requestAndWait(requestMessage, queue);
            if (message != null) {
                return message.getBody(ResponseMessage.class);
            } else {
                // We don't want to wait forever. If the message is null,
                // it means that the set timeout has been reached.
                // Let backend handle it. The client will get a handle (uuid) which they
                // can use to check if it's done.
                return new ResponseMessage(messageId, aStatusOf(ACCEPTED));
            }
        } catch (JMSException e) {
            log.error(e);
        }

        return new ResponseMessage(messageId, aStatusOf(SERVER_ERROR));
    }
}
