package ru.otus.library.service;

import ru.otus.library.model.Book;

import java.util.List;

public interface BookService {

    Book getById(String id);

    List<Book> getAll();

    Book create(String title, String genreId, String authorId);

    Book update(String id, String title);

    void deleteById(String id);
}
