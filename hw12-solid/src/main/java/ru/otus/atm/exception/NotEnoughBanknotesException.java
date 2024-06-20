package ru.otus.atm.exception;

public class NotEnoughBanknotesException extends RuntimeException {
    public NotEnoughBanknotesException(String message) {
        super(message);
    }
}
