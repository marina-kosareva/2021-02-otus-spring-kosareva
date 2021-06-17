package ru.otus.batch.service;

import ru.otus.batch.entities.AuthorEntity;
import ru.otus.batch.model.AuthorDocument;

public interface AuthorService {

    AuthorDocument getOrCreate(AuthorEntity entity);
}
