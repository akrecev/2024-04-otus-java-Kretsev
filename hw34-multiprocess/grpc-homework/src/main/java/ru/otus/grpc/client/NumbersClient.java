package ru.otus.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.NumberRequest;
import ru.otus.grpc.NumbersServiceGrpc;

public class NumbersClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final long FIRST_VALUE = 1;
    private static final long LAST_VALUE = 20;
    private static final int LOOP_LIMIT = 40;
    private static final int CLIENT_INITIAL_DELAY_ON_SECONDS = 0;
    private static final int CLIENT_PERIOD_ON_SECONDS = 1;

    private long value = 0;

    public static void main(String[] args) {
        log.info("Client is started...");

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        NumbersServiceGrpc.NumbersServiceStub asyncClient = NumbersServiceGrpc.newStub(managedChannel);

        new NumbersClient().clientAction(asyncClient);

        log.info("Client is shutting down...");
        managedChannel.shutdown();
    }

    @SuppressWarnings("java:S2095")
    private void clientAction(NumbersServiceGrpc.NumbersServiceStub asyncClient) {
        NumberRequest numberRequest = makeNumberRequest();
        ClientStreamObserver clientStreamObserver = new ClientStreamObserver();
        asyncClient.generateNumbers(numberRequest, clientStreamObserver);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        final AtomicInteger currentValue = new AtomicInteger(0);

        Runnable task = () -> {
            int counter = currentValue.incrementAndGet();
            long valForPrint = computeAndUpdateValue(clientStreamObserver);
            log.info("currentValue:{}", valForPrint);

            if (counter == LOOP_LIMIT) {
                executor.shutdown();
                log.info("Client finished loop, shutting down scheduler...");
            }
        };

        executor.scheduleAtFixedRate(task, CLIENT_INITIAL_DELAY_ON_SECONDS, CLIENT_PERIOD_ON_SECONDS, TimeUnit.SECONDS);
    }

    private synchronized long computeAndUpdateValue(ClientStreamObserver clientStreamObserver) {
        long nextValue = this.value + clientStreamObserver.getLastValueAndReset() + 1;
        this.value = nextValue;

        return nextValue;
    }

    private NumberRequest makeNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();
    }
}
