package ru.otus.books.repository;

import ru.otus.books.model.Author;

import java.util.List;

public interface AuthorRepository {

    Author getById(Long id);

    List<Author> getAll();

    Author create(Author author);

    Author update(Author author);

    int deleteById(Long id);
}
