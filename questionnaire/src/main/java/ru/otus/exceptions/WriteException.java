package ru.otus.exceptions;

public class WriteException extends RuntimeException {

    public WriteException(String message, Throwable e) {
        super(message, e);
    }
}
