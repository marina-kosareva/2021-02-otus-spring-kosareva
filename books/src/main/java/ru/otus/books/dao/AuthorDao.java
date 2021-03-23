package ru.otus.books.dao;

import ru.otus.books.model.Author;

import java.util.List;

public interface AuthorDao {

    Author getById(Long id);

    List<Author> getAll();

    Long create(Author author);

    int update(Long id, String firstName, String lastName);

    int deleteById(Long id);
}
