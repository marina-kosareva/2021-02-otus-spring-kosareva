package ru.otus.exceptions;

public class QuestionsLoadingException extends RuntimeException {

    public QuestionsLoadingException(String message) {
        super(message);
    }

    public QuestionsLoadingException(String message, Throwable e) {
        super(message, e);
    }
}
