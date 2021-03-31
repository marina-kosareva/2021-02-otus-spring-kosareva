package ru.otus.books.service;

import ru.otus.books.model.Author;

import java.util.List;

public interface AuthorService {

    Author getById(Long id);

    List<Author> getAll();

    Author create(String firstName, String lastName);

    Author update(Long id, String firstName, String lastName);

    int deleteById(Long id);
}
