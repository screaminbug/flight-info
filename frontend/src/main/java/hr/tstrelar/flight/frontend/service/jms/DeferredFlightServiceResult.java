package hr.tstrelar.flight.frontend.service.jms;

import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.RequestMessage;
import hr.tstrelar.flight.model.ResponseMessage;
import hr.tstrelar.flight.model.SearchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public final class DeferredFlightServiceResult
        extends DeferredResult<ResponseEntity<ResponseMessage>> {

    @Value("${jms.sync.timeout}")
    private static Long defaultTimeout;

    private RequestMessage request;

    private DeferredFlightServiceResult(Long timeout) {
        super(timeout);
    }

    public DeferredResult<ResponseEntity<ResponseMessage>> callWith(Function<RequestMessage, ResponseMessage> serviceCall) {
        ForkJoinPool.commonPool().submit(
            () -> setResult(ResponseEntity.ok(serviceCall.apply(request)))
        );

        return this;
    }

    public static final class Builder {
        private Long timeout = defaultTimeout;

        public static <T extends RequestMessage> Builder create() {
            return new Builder();
        }

        public Builder withTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Final withRequest(FlightDto request) {
            final RequestMessage requestMessage = new RequestMessage(request);
            return new Final(requestMessage, timeout);
        }

        public Final withRequest(FlightDto request, UUID messageId) {
            final RequestMessage requestMessage = new RequestMessage(request);
            requestMessage.setMessageId(messageId);
            return new Final(requestMessage, timeout);
        }

        public Final withRequest(SearchDto request) {
            final RequestMessage requestMessage = new RequestMessage(request);
            return new Final(requestMessage, timeout);
        }

        public Final withRequest(UUID request) {
            final RequestMessage requestMessage = new RequestMessage(request);
            return new Final(requestMessage, timeout);
        }



        public static final class Final {
            private RequestMessage request;
            private Long timeout;

            Final(RequestMessage request, Long timeout) {
                this.request = request;
                this.timeout = timeout;
            }

            public DeferredFlightServiceResult build() {
                DeferredFlightServiceResult deferredFlightServiceResult =
                        new DeferredFlightServiceResult(timeout);
                deferredFlightServiceResult.request = this.request;
                return deferredFlightServiceResult;
            }
        }
    }
}
