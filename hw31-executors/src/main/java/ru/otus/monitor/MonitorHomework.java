package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorHomework {
    private static final Logger log = LoggerFactory.getLogger(MonitorHomework.class);
    private static final int TOP = 10;
    private static final int DELAY_ON_MILLISECONDS = 500;
    private static final String FIRST_THREAD_NAME = "Thread-1";
    private static final String SECOND_THREAD_NAME = "Thread-2";
    private static boolean isThread1Turn = true;
    private final Object lock = new Object();

    public static void main(String[] args) {
        var pusher = new MonitorHomework();

        new Thread(() -> pusher.push(FIRST_THREAD_NAME, true)).start();
        new Thread(() -> pusher.push(SECOND_THREAD_NAME, false)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(DELAY_ON_MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static int[] generateSequence() {
        int[] array = new int[TOP * 2 - 2];
        for (int i = 0; i < TOP; i++) {
            array[i] = i + 1;
            if (i > 0) {
                array[array.length - i] = i + 1;
            }
        }

        return array;
    }

    private void push(String name, boolean isThreadFirst) {
        synchronized (lock) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (int i : generateSequence()) {
                        while (isThread1Turn != isThreadFirst) {
                            lock.wait();
                        }
                        sleep();
                        log.info("{}: {}", name, i);
                        isThread1Turn = !isThread1Turn;
                        lock.notifyAll();
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
