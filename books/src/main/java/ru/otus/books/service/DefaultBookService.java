package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.model.Book;
import ru.otus.books.repository.BookRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final BookRepository repository;

    @Override
    public Book getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Book> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public Book create(String title, Long genreId, Long authorId) {
        return repository.create(title, genreId, authorId);
    }

    @Override
    @Transactional
    public int update(Long id, String title) {
        return repository.update(id, title);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
