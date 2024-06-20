package ru.otus.homework.framework;

import static ru.otus.homework.annotations.TestAnnotationName.*;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.annotations.TestAnnotationName;
import ru.otus.reflection.ReflectionHelper;

@UtilityClass
@Slf4j
public class TestFramework {
    public static void runTest(String className) throws ClassNotFoundException {
        Class<?> testingClass = Class.forName(className);
        Map<TestAnnotationName, List<Method>> methods = getMethods(testingClass);
        runTestMethods(methods, testingClass);
    }

    private static Map<TestAnnotationName, List<Method>> getMethods(Class<?> testingClass) {
        Map<TestAnnotationName, List<Method>> methods = new EnumMap<>(TestAnnotationName.class);
        for (Method method : testingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                putMethod(methods, BEFORE, method);
            } else if (method.isAnnotationPresent(Test.class)) {
                putMethod(methods, TEST, method);
            } else if (method.isAnnotationPresent(After.class)) {
                putMethod(methods, AFTER, method);
            }
        }
        return methods;
    }

    private static void runTestMethods(Map<TestAnnotationName, List<Method>> methods, Class<?> testingClass) {
        int passed = 0;
        int failed = 0;
        for (Method testMethod : methods.get(TEST)) {
            Object testingEntity = ReflectionHelper.instantiate(testingClass);
            try {
                methods.get(BEFORE).forEach(method -> ReflectionHelper.callMethod(testingEntity, method.getName()));
                ReflectionHelper.callMethod(testingEntity, testMethod.getName());
                passed++;

                log.debug("execute method: {}", testMethod.getName());

            } catch (Exception e) {
                failed++;

                log.debug("failed method: {}", testMethod.getName());

            } finally {
                methods.get(AFTER).forEach(method -> ReflectionHelper.callMethod(testingEntity, method.getName()));
            }
        }
        log.debug("Tests passed: {}, tests failed: {}, total: {}", passed, failed, passed + failed);
    }

    private static void putMethod(
            Map<TestAnnotationName, List<Method>> methods, TestAnnotationName annotationName, Method method) {
        methods.put(
                annotationName,
                methods.get(annotationName) == null
                        ? List.of(method)
                        : Stream.concat(methods.get(annotationName).stream(), Stream.of(method))
                                .toList());
    }
}
