package ru.otus.books.dao;

import ru.otus.books.model.Book;

import java.util.List;

public interface BookDao {

    Book getById(Long id);

    List<Book> getAll();

    Long create(String title, Long genreId, Long authorId);

    int update(Long id, String title);

    int deleteById(Long id);
}
