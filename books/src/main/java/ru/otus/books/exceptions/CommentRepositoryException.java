package ru.otus.books.exceptions;

public class CommentRepositoryException extends RuntimeException {

    public CommentRepositoryException(String message, Throwable e) {
        super(message, e);
    }
}
