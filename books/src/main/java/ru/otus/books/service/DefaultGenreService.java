package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.model.Genre;
import ru.otus.books.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultGenreService implements GenreService {

    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Genre getById(Long id) {
        return repository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public Genre create(String title) {
        return repository.create(Genre.builder()
                .title(title)
                .build());
    }

    @Override
    @Transactional
    public Genre update(Long id, String title) {
        return repository.update(id, title);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
