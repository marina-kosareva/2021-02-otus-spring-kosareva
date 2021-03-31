package ru.otus.books.repository;

import ru.otus.books.model.Book;

import java.util.List;

public interface BookRepository {

    Book getById(Long id);

    List<Book> getAll();

    Book create(Book book);

    Book update(Long id, String title);

    int deleteById(Long id);
}
