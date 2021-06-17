package ru.otus.batch.service;

import ru.otus.batch.entities.BookEntity;
import ru.otus.batch.model.BookDocument;

public interface BookService {

    BookDocument convert(BookEntity entity);
}
