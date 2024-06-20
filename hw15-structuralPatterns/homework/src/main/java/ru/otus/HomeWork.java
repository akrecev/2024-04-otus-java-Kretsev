package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.processor.ProcessorReplaceFields;
import ru.otus.processor.homework.processor.TimeProcessor;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элементов "to do" создать new ComplexProcessor и обработать сообщение
        */
        var processors =
                List.of(new ProcessorReplaceFields(), new TimeProcessor(() -> LocalDateTime.of(0, 1, 1, 1, 1, 1)));
        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);
        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("1", "2"));

        var message = new Message.Builder(1L)
                .field11("field 11")
                .field12("field 12")
                .field13(objectForMessage)
                .build();

        var resultMessage = complexProcessor.handle(message);
        var messageFromHistory = historyListener.findMessageById(1L).get();

        System.out.println(message);
        System.out.println("=====================================");
        System.out.println(resultMessage);
        System.out.println("=====================================");
        System.out.println(messageFromHistory);
    }
}
