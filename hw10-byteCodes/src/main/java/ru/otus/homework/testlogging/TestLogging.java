package ru.otus.homework.testlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.Log;

public class TestLogging implements TestLoggingInterface {
    private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);

    @Override
    @Log
    public void calculation(int param1) {
        logger.info("Calculating 1 parameter: int {}", param1);
    }

    @Override
    public void calculation(int param1, int param2) {
        logger.info("Calculating 2 parameters: int {}, int {}", param1, param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        logger.info("Calculating 3 parameters: int {}, int {}, String {}", param1, param2, param3);
    }
}
