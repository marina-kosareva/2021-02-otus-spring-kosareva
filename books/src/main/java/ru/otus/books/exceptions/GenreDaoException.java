package ru.otus.books.exceptions;

public class GenreDaoException extends RuntimeException {

    public GenreDaoException(String message, Throwable e) {
        super(message, e);
    }

}

