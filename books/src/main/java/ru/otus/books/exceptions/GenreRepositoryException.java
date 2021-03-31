package ru.otus.books.exceptions;

public class GenreRepositoryException extends RuntimeException {

    public GenreRepositoryException(String message) {
        super(message);
    }

    public GenreRepositoryException(String message, Throwable e) {
        super(message, e);
    }

}

