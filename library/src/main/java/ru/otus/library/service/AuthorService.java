package ru.otus.library.service;

import ru.otus.library.model.Author;

import java.util.List;

public interface AuthorService {

    Author getById(String id);

    List<Author> getAll();

    Author create(String firstName, String lastName);

    Author update(String id, String firstName, String lastName);

    void deleteById(String id);
}
