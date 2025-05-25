package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final PriorityBlockingQueue<SensorData> dataBuffer;
    private final Lock lock = new ReentrantLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        lock.lock();
        try {
            dataBuffer.add(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        lock.lock();
        try {
            flushInternal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        lock.lock();
        try {
            flush();
        } finally {
            lock.unlock();
        }
    }

    private void flushInternal() {
        try {
            List<SensorData> bufferedData = new ArrayList<>();
            if (dataBuffer.isEmpty()) {
                return;
            }

            SensorData polled;
            while ((polled = dataBuffer.poll()) != null) {
                bufferedData.add(polled);
            }

            if (!bufferedData.isEmpty()) {
                writer.writeBufferedData(bufferedData);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }
}
