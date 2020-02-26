package hr.tstrelar.flight.frontend.helpers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

import static hr.tstrelar.flight.frontend.helpers.Constants.*;


@Component
public class SyncRequestor<T extends Serializable> {
    private final JmsTemplate jmsTemplate;
    @Setter
    private Integer timeout;
    @Setter
    private String correlationId;

    @Autowired
    public SyncRequestor(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Message requestAndWait(final T request, String destination) {
        assert (correlationId != null);
        return jmsTemplate.execute(
                new SessionCallbackWithConsumerProducer<>(
                        request, destination, timeout, correlationId, jmsTemplate.getDestinationResolver()
                ),true );
    }
    private static final class SessionCallbackWithConsumerProducer<T extends Serializable> implements SessionCallback<Message> {
        private final int timeout;
        private final T msg;
        private final String correlationId;
        private final DestinationResolver destinationResolver;
        private final String destination;

        public SessionCallbackWithConsumerProducer(
                final T msg,
                String destination,
                Integer timeout,
                String correlationId,
                final DestinationResolver destinationResolver) {
            this.msg = msg;
            this.destination = destination;
            this.destinationResolver = destinationResolver;
            this.timeout = timeout != null ? timeout : DEFAULT_RECEIVE_TIMEOUT;
            this.correlationId = correlationId;
        }

        public Message doInJms(final Session session) throws JMSException {
            MessageConsumer consumer = null;
            MessageProducer producer = null;
            try {
                final Destination requestDestination =
                        destinationResolver.resolveDestinationName(
                                session,
                                destination + REQUEST_SUFFIX,
                                true
                        );
                final Destination replyDestination =
                        destinationResolver.resolveDestinationName(
                                session,
                                destination + RESPONSE_SUFFIX,
                                true);

                consumer = session.createConsumer(replyDestination, CorrelationSelector.select(correlationId));

                final ObjectMessage objectMessage = session.createObjectMessage(msg);
                objectMessage.setJMSCorrelationID(correlationId);
                objectMessage.setJMSReplyTo(replyDestination);

                producer = session.createProducer(requestDestination);
                producer.send(objectMessage);
                return consumer.receive(timeout); // blocking!

            } finally {
                if (consumer != null) { JmsUtils.closeMessageConsumer(consumer); }
                if (producer != null) { JmsUtils.closeMessageProducer(producer); }
            }
        }
    }
}
