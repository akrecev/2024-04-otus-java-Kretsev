package ru.otus.grpc.server;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.NumberRequest;
import ru.otus.grpc.NumberResponse;
import ru.otus.grpc.NumbersServiceGrpc;

public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumbersServiceImpl.class);
    public static final long SERVER_INITIAL_DELAY_ON_SECONDS = 0;
    public static final long SERVER_PERIOD_ON_SECONDS = 2;

    @Override
    @SuppressWarnings("java:S2095")
    public void generateNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info(
                "Request for the new sequence of numbers, firstValue(): {}, lastValue: {}",
                request.getFirstValue(),
                request.getLastValue());
        AtomicLong currentValue = new AtomicLong(request.getFirstValue());
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            long value = currentValue.incrementAndGet();
            NumberResponse response =
                    NumberResponse.newBuilder().setNumber(value).build();
            responseObserver.onNext(response);
            if (value == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("Sequence of numbers finished");
            }
        };
        executor.scheduleAtFixedRate(task, SERVER_INITIAL_DELAY_ON_SECONDS, SERVER_PERIOD_ON_SECONDS, TimeUnit.SECONDS);
    }
}
