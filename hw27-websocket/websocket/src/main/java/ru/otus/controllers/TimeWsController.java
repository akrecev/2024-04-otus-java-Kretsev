package ru.otus.controllers;

import static ru.otus.ApplConfig.DATE_TIME_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TimeWsController {
    private final SimpMessagingTemplate template;

    public TimeWsController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 1000)
    public void broadcastCurrentTime() {
        template.convertAndSend(
                "/topic/currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    }
}
