package ru.otus.library.service;

import ru.otus.library.model.Genre;

import java.util.List;

public interface GenreService {

    Genre getById(String id);

    List<Genre> getAll();

    Genre create(String title);

    Genre update(String id, String title);

    void deleteById(String id);
}
