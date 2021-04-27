package ru.otus.library.service;

import ru.otus.library.dto.BookDto;
import ru.otus.library.model.Book;

import java.util.List;

public interface BookService {

    BookDto getBookDtoById(String id);

    List<BookDto> getAll();

    BookDto create(String title, String genreId, String authorId);

    BookDto update(String id, String title);

    void deleteById(String id);
}
