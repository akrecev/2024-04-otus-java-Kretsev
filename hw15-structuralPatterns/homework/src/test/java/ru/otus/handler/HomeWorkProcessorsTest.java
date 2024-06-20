package ru.otus.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.exception.EvenSecondException;
import ru.otus.processor.homework.processor.ProcessorReplaceFields;
import ru.otus.processor.homework.processor.TimeProcessor;

public class HomeWorkProcessorsTest {

    @Test
    @DisplayName("Testing replace of fields")
    void replaceFieldsProcessorTest() {
        // given
        var field11 = "field 11";
        var field12 = "field 12";
        var processor = new ProcessorReplaceFields();

        // when
        var message = processor.process(
                new Message.Builder(1L).field11(field11).field12(field12).build());

        // then
        assertThat(message.getField11()).isEqualTo(field12);
        assertThat(message.getField12()).isEqualTo(field11);
    }

    @Test
    @DisplayName("Testing thrown exception on even second run")
    void throwEventSecondExceptionTest() {
        // when
        var processor = new TimeProcessor(() -> LocalDateTime.of(100500, 12, 31, 23, 59, 58));

        // then
        assertThatExceptionOfType(EvenSecondException.class)
                .isThrownBy(() -> processor.process(new Message.Builder(1L).build()));
    }

    @Test
    @DisplayName("Testing TimeProcessor runs on odd second")
    void runTimeProcessorOnOddSecond() {
        // given
        var field12 = "field 12";
        var processor = new TimeProcessor(() -> LocalDateTime.of(100500, 12, 31, 23, 59, 59));

        // when
        var message = processor.process(new Message.Builder(1L).field12(field12).build());

        // then
        assertThat(message.getField12()).isEqualTo(field12);
    }
}
