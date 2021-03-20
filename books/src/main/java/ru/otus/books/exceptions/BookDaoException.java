package ru.otus.books.exceptions;

public class BookDaoException extends RuntimeException {

    public BookDaoException(String message, Throwable e) {
        super(message, e);
    }

}

