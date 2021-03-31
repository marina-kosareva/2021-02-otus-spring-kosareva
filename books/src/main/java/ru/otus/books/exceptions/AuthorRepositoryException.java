package ru.otus.books.exceptions;

public class AuthorRepositoryException extends RuntimeException {

    public AuthorRepositoryException(String message) {
        super(message);
    }

    public AuthorRepositoryException(String message, Throwable e) {
        super(message, e);
    }

}

