package ru.otus.exceptions;

public class ReadException extends RuntimeException {

    public ReadException(String message, Throwable e) {
        super(message, e);
    }
}
