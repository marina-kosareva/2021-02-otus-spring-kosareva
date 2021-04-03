package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.exceptions.BookRepositoryException;
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
    public Book create(String title, Long genreId, Long authorId) {
        try {
            return repository.saveAndFlush(Book.builder()
                    .title(title)
                    .author(authorService.getById(authorId))
                    .genre(genreService.getById(genreId))
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new BookRepositoryException("error during book creating", ex);
        }
    }

    @Override
    @Transactional
    public Book update(Long id, String title) {
        try {
            Book existing = getById(id);
            existing.setTitle(title);
            return repository.saveAndFlush(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new BookRepositoryException("error during book updating", ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
