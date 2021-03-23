package ru.otus.books.exceptions;

public class AuthorDaoException extends RuntimeException {

    public AuthorDaoException(String message, Throwable e) {
        super(message, e);
    }

}

