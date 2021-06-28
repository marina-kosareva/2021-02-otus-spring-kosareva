package ru.otus.integration.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        violations.forEach(violation ->
                builder.append(violation.getPropertyPath())
                        .append(violation.getMessage())
                        .append(" "));
        return ResponseEntity.unprocessableEntity()
                .body("Validation error: " + builder);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.unprocessableEntity()
                .body("Error: " + ex.getMessage());
    }
}
