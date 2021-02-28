package ru.otus.exceptions;

public class CsvParseException extends RuntimeException {

    public CsvParseException(String message) {
        super(message);
    }
}
