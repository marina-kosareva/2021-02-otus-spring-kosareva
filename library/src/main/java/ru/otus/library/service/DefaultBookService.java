package ru.otus.library.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.BookDto;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;

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
    public List<BookDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::bookToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto create(String title, String genreId, String authorId) {
        return mapper.bookToBookDto(repository.insert(Book.builder()
                .title(title)
                .author(authorService.getById(authorId))
                .genre(genreService.getById(genreId))
                .build()));
    }

    @Override
    @Transactional
    public BookDto update(String id, String title, Long version) {
        Book existing = getById(id);
        existing.setVersion(version);
        existing.setTitle(title);
        return mapper.bookToBookDto(repository.save(existing));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private Book getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookRepositoryException("error getting book by id " + id));
    }
}
