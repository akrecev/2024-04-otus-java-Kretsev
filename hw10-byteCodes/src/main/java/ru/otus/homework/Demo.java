package ru.otus.homework;

import ru.otus.homework.proxy.LoggingIoc;
import ru.otus.homework.testlogging.TestLogging;
import ru.otus.homework.testlogging.TestLoggingAnother;
import ru.otus.homework.testlogging.TestLoggingInterface;

public class Demo {
    public static void main(String[] args) {
        action();
    }

    public static void action() {
        TestLoggingInterface testLogging = LoggingIoc.createTestLoggingClass(new TestLogging());
        testLogging.calculation(1);
        testLogging.calculation(2, 3);
        testLogging.calculation(4, 5, "Hello!!!");
        testLogging.calculation(6);
        testLogging.calculation(7, 8);
        testLogging.calculation(9, 10, "Hello again!!!");
        testLogging.calculation(11);
        testLogging.calculation(12, 13);
        testLogging.calculation(14, 15, "Hello again again!!!");
        testLogging.calculation(16);
        testLogging.calculation(17, 18);
        testLogging.calculation(19, 20, "Hello again again again!!!");

        TestLoggingInterface anotherTestLogging = LoggingIoc.createTestLoggingClass(new TestLoggingAnother());
        anotherTestLogging.calculation(100500);
        anotherTestLogging.calculation(12, 34);
        anotherTestLogging.calculation(5, 5, "Salut!!!");
    }
}
