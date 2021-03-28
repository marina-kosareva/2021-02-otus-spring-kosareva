package ru.otus.books.repository;

import ru.otus.books.model.Genre;

import java.util.List;

public interface GenreRepository {

    Genre getById(Long id);

    List<Genre> getAll();

    Genre create(Genre genre);

    int update(Long id, String title);

    int deleteById(Long id);
}
