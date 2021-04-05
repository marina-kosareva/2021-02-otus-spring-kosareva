package ru.otus.books.service;

import ru.otus.books.model.Genre;

import java.util.List;

public interface GenreService {

    Genre getById(Long id);

    List<Genre> getAll();

    Genre create(String title);

    Genre update(Long id, String title);

    void deleteById(Long id);
}
