package ru.otus.processor.homework.processor;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorReplaceFields implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
