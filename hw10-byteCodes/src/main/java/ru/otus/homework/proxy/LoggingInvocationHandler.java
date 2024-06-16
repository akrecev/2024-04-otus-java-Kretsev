package ru.otus.homework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.Log;
import ru.otus.homework.testlogging.TestLoggingInterface;

class LoggingInvocationHandler implements InvocationHandler {
    public static final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);
    private final TestLoggingInterface loggingClass;
    private final Set<Method> loggingMethods = new HashSet<>();

    LoggingInvocationHandler(TestLoggingInterface loggingClass) {
        this.loggingClass = loggingClass;
        for (Method method : loggingClass.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                try {
                    loggingMethods.add(getInterfaceMethod(method));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (loggingMethods.contains(method)) {
            logger.info("executed method: {}, param: {}", method.getName(), args);
        }
        return method.invoke(loggingClass, args);
    }

    private static Method getInterfaceMethod(Method method) throws NoSuchMethodException {
        return TestLoggingInterface.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }
}
