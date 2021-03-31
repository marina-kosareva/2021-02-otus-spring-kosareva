package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.model.Book;
import ru.otus.books.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return repository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public Book create(String title, Long genreId, Long authorId) {
        return repository.create(Book.builder()
                .title(title)
                .author(authorService.getById(authorId))
                .genre(genreService.getById(genreId))
                .build());
    }

    @Override
    @Transactional
    public Book update(Long id, String title) {
        return repository.update(id, title);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
