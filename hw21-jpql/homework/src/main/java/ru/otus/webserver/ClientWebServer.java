package ru.otus.webserver;

@SuppressWarnings({"squid:S112"})
public interface ClientWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
