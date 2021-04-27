package ru.otus.library.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.library.exceptions.AuthorRepositoryException;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.exceptions.GenreRepositoryException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(BookRepositoryException.class)
    public String handleBookRepositoryException(BookRepositoryException ex, Model model) {
        return errorResponse(model, ex);
    }

    @ExceptionHandler(AuthorRepositoryException.class)
    public String handleAuthorRepositoryException(AuthorRepositoryException ex, Model model) {
        return errorResponse(model, ex);
    }

    @ExceptionHandler(GenreRepositoryException.class)
    public String handleGenreRepositoryException(GenreRepositoryException ex, Model model) {
        return errorResponse(model, ex);
    }

    private String errorResponse(Model model, Throwable ex) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("exception", ex);
        return "error";
    }
}
