package ru.otus.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.BookDto;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto getBookDtoById(String id) {
        return mapper.bookToBookDto(getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "books", fallbackMethod = "getAllFallback")
    public List<BookDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::bookToBookDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getAllFallback() {
        return new ArrayList<>();
    }

    @Override
    @Transactional
    @HystrixCommand(commandKey = "books", raiseHystrixExceptions = {HystrixException.RUNTIME_EXCEPTION})
    public BookDto create(String title, String genreId, String authorId) {
        return mapper.bookToBookDto(repository.insert(Book.builder()
                .title(title)
                .author(authorService.getById(authorId))
                .genre(genreService.getById(genreId))
                .build()));
    }

    @Override
    @Transactional
    @HystrixCommand(commandKey = "books", raiseHystrixExceptions = {HystrixException.RUNTIME_EXCEPTION})
    public BookDto update(String id, String title, Long version) {
        Book existing = getById(id);
        existing.setVersion(version);
        existing.setTitle(title);
        return mapper.bookToBookDto(repository.save(existing));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @HystrixCommand(commandKey = "books", fallbackMethod = "deleteFallback")
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void deleteFallback(String id) {
    }

    private Book getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookRepositoryException("error getting book by id " + id));
    }
}
