package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.BookDao;
import ru.otus.books.model.Book;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final BookDao dao;

    @Override
    public Book getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public List<Book> getAll() {
        return dao.getAll();
    }

    @Override
    public Long create(String title, Long genreId, Long authorId) {
        return dao.create(title, genreId, authorId);
    }

    @Override
    public int update(Long id, String title) {
        return dao.update(id, title);
    }

    @Override
    public int deleteById(Long id) {
        return dao.deleteById(id);
    }
}
