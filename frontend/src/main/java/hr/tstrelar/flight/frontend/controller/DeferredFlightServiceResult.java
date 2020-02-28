package hr.tstrelar.flight.frontend.controller;

import hr.tstrelar.flight.frontend.exception.FlightServiceException;
import hr.tstrelar.flight.frontend.model.FlightResponse;
import hr.tstrelar.flight.frontend.service.PerformServiceCall;
import hr.tstrelar.flight.model.FlightDto;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ForkJoinPool;

public final class DeferredFlightServiceResult<T extends FlightDto, R extends FlightResponse>
        extends DeferredResult<ResponseEntity<R>> {

    private T request;
    private Class<R> responseClass;

    private DeferredFlightServiceResult(Long timeout, Class<R> responseClass) {
        super(timeout);
        this.responseClass = responseClass;
    }

    public DeferredResult<ResponseEntity<R>> callWith(PerformServiceCall<T, R> serviceCall) {
        ForkJoinPool.commonPool().submit(
            () -> {
                try {
                    setResult(ResponseEntity.ok(serviceCall.apply(request)));
                } catch (FlightServiceException e) {
                    R response = getResponseInstance();
                    assert(e.getCause() != null);
                    response.setErrorMessage(e.getCause().getMessage());;
                }
            }
        );
        final R timeoutResponse = getResponseInstance();
        timeoutResponse.setErrorMessage("Waited longer than expected. Try Again...");
        onTimeout(() -> setResult(ResponseEntity.accepted().body(timeoutResponse)));
        return this;
    }

    @SneakyThrows
    private R getResponseInstance() {
        return responseClass.newInstance();
    }

    public static final class Builder<T extends FlightDto, R extends FlightResponse> {
        private Long timeout;
        private Class<R> responseClass;

        private Builder(Long timeout, Class<R> responseClass) {
            this.timeout = timeout;
            this.responseClass = responseClass;
        }

        public static <T extends FlightDto, R extends FlightResponse> Builder<T, R> create(Long timeout, Class<R> responseClass) {
            return new Builder<T, R>(timeout, responseClass);
        }

        public Final<T, R> withRequest(T request) {
            return new Final<T, R>(request, timeout, responseClass);
        }

        public static final class Final<T extends FlightDto, R extends FlightResponse> {
            private T request;
            private Long timeout;
            private Class<R> responseClass;

            Final(T request, Long timeout, Class<R> responseClass) {
                this.request = request;
                this.timeout = timeout;
                this.responseClass = responseClass;
            }

            public DeferredFlightServiceResult<T, R> build() {
                DeferredFlightServiceResult<T, R> deferredFlightServiceResult = new DeferredFlightServiceResult<>(timeout, responseClass);
                deferredFlightServiceResult.request = this.request;
                return deferredFlightServiceResult;
            }
        }
    }
}
