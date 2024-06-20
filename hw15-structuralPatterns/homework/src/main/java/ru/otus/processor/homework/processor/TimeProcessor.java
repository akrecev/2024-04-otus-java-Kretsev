package ru.otus.processor.homework.processor;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.exception.EvenSecondException;

public class TimeProcessor implements Processor {

    private final TimeProvider timeProvider;

    public TimeProcessor(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        if (timeProvider.getTime().getSecond() % 2 == 0) {
            throw new EvenSecondException(String.format(
                    "Program start on even second: %s", timeProvider.getTime().toString()));
        }
        return message;
    }
}
