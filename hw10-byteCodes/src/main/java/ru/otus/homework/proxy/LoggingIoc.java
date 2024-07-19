package ru.otus.homework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import ru.otus.homework.testlogging.TestLoggingInterface;

public class LoggingIoc {
    private LoggingIoc() {}

    public static TestLoggingInterface createTestLoggingClass(Object loggingClass) {
        InvocationHandler handler = new LoggingInvocationHandler(loggingClass);
        return (TestLoggingInterface) Proxy.newProxyInstance(
                LoggingIoc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }
}
