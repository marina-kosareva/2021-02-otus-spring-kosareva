package ru.otus.books.dao;

import ru.otus.books.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre getById(Long id);

    List<Genre> getAll();

    Long create(Genre genre);

    int update(Long id, String title);

    int deleteById(Long id);
}
