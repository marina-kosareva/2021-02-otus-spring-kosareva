package ru.otus.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.library.exceptions.AuthorRepositoryException;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.exceptions.GenreRepositoryException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(BookRepositoryException.class)
    public ResponseEntity<String> handleBookRepositoryException(BookRepositoryException ex) {
        return errorResponse("BookException: ", ex);
    }

    @ExceptionHandler(AuthorRepositoryException.class)
    public ResponseEntity<String> handleAuthorRepositoryException(AuthorRepositoryException ex) {
        return errorResponse("AuthorException: ", ex);
    }

    @ExceptionHandler(GenreRepositoryException.class)
    public ResponseEntity<String> handleGenreRepositoryException(GenreRepositoryException ex) {
        return errorResponse("GenreException: ", ex);
    }

    private ResponseEntity<String> errorResponse(String message, Throwable ex) {
        return ResponseEntity.badRequest().body(message + ex.getMessage());
    }
}
