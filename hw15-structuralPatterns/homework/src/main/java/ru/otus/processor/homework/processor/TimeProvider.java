package ru.otus.processor.homework.processor;

import java.time.LocalDateTime;

@FunctionalInterface
public interface TimeProvider {
    LocalDateTime getTime();
}
