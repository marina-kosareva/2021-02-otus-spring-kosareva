package ru.otus.books.service;

import ru.otus.books.model.Book;

import java.util.List;

public interface BookService {

    Book getById(Long id);

    List<Book> getAll();

    Book create(String title, Long genreId, Long authorId);

    int update(Long id, String title);

    int deleteById(Long id);
}
