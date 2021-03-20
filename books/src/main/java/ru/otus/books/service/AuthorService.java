package ru.otus.books.service;

import ru.otus.books.model.Author;

import java.util.List;

public interface AuthorService {

    Author getById(Long id);

    List<Author> getAll();

    Long create(String firstName, String lastName);

    int update(Long id, String firstName, String lastName);

    int deleteById(Long id);
}
