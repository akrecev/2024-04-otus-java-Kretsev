package ru.otus.webserver;

public interface ClientWebServer {
    void start() throws Exception;
    void join() throws Exception;
    void stop() throws Exception;
}
