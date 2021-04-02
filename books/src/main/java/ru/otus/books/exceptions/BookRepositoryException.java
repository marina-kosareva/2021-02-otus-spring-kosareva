package ru.otus.books.exceptions;

public class BookRepositoryException extends RuntimeException {

    public BookRepositoryException(String message, Throwable e) {
        super(message, e);
    }

}

