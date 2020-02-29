package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.model.FlightDto;
import hr.tstrelar.flight.model.FlightMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public final class DeferredFlightServiceResult<T extends FlightDto>
        extends DeferredResult<ResponseEntity<FlightMessage>> {

    @Value("${jms.sync.timeout}")
    private static Long defaultTimeout;

    private T request;

    private DeferredFlightServiceResult(Long timeout) {
        super(timeout);
    }

    public DeferredResult<ResponseEntity<FlightMessage>> callWith(Function<T, FlightMessage> serviceCall) {
        ForkJoinPool.commonPool().submit(
            () -> setResult(ResponseEntity.ok(serviceCall.apply(request)))
        );

        return this;
    }

    public static final class Builder<T extends FlightDto> {
        private Long timeout = defaultTimeout;

        public static <T extends FlightDto> Builder<T> create() {
            return new Builder<T>();
        }

        public Builder<T> withTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Final<T> withRequest(T request) {
            return new Final<T>(request, timeout);
        }

        public static final class Final<T extends FlightDto> {
            private T request;
            private Long timeout;

            Final(T request, Long timeout) {
                this.request = request;
                this.timeout = timeout;
            }

            public DeferredFlightServiceResult<T> build() {
                DeferredFlightServiceResult<T> deferredFlightServiceResult =
                        new DeferredFlightServiceResult<>(timeout);
                deferredFlightServiceResult.request = this.request;
                return deferredFlightServiceResult;
            }
        }
    }
}
