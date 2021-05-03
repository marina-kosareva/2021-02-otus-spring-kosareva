package ru.otus.library.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.library.exceptions.AuthorRepositoryException;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.exceptions.GenreRepositoryException;

import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.unprocessableEntity()
                .body("Validation error: " + ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + "-" + error.getDefaultMessage())
                        .collect(Collectors.joining()));
    }

    private ResponseEntity<String> errorResponse(String message, Throwable ex) {
        return ResponseEntity.badRequest().body(message + ex.getMessage());
    }
}
