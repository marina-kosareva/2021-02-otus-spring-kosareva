package ru.otus.batch.service;

import ru.otus.batch.entities.GenreEntity;
import ru.otus.batch.model.GenreDocument;

public interface GenreService {
    GenreDocument getOrCreate(GenreEntity entity);
}
