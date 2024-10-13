package ru.otus.appcontainer.exception;

public class AppComponentsContainerException extends RuntimeException {
    public AppComponentsContainerException() {
        super();
    }

    public AppComponentsContainerException(String message) {
        super(message);
    }

    public AppComponentsContainerException(Throwable cause) {
        super(cause);
    }

    public AppComponentsContainerException(String message, Throwable cause) {
        super(message, cause);
    }
}
