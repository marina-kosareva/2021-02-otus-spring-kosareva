package ru.otus.library.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public Book getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookRepositoryException("error getting book by id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Book create(String title, String genreId, String authorId) {
        return repository.insert(Book.builder()
                .title(title)
                .author(authorService.getById(authorId))
                .genre(genreService.getById(genreId))
                .build());
    }

    @Override
    @Transactional
    public Book update(String id, String title) {
        Book existing = getById(id);
        existing.setTitle(title);
        return repository.save(existing);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
