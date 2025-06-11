package ru.otus.grpc.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.NumberResponse;

public class ClientStreamObserver implements StreamObserver<NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private long lastValue = 0;

    @Override
    public void onNext(NumberResponse numberResponse) {
        log.info("new value: {}", numberResponse.getNumber());
        setLastValue(numberResponse.getNumber());
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Got error");
    }

    @Override
    public void onCompleted() {
        log.info("Request completed");
    }

    private synchronized void setLastValue(long value) {
        this.lastValue = value;
    }

    public synchronized long getLastValueAndReset() {
        long lastValuePrev = this.lastValue;
        this.lastValue = 0;
        return lastValuePrev;
    }
}
