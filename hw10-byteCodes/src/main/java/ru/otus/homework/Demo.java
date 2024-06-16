package ru.otus.homework;

import ru.otus.homework.proxy.LoggingIoc;
import ru.otus.homework.testlogging.TestLogging;
import ru.otus.homework.testlogging.TestLoggingInterface;

public class Demo {
    public static void main(String[] args) {
        action();
    }

    public static void action() {
        TestLoggingInterface testLogging = LoggingIoc.createTestLoggingClass(new TestLogging());
        testLogging.calculation(6);
        testLogging.calculation(6, 2);
        testLogging.calculation(6, 2, "Hello!!!");
    }
}
