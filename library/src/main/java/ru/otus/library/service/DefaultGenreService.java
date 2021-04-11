package ru.otus.library.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exceptions.GenreRepositoryException;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultGenreService implements GenreService {

    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Genre getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new GenreRepositoryException("error getting genre by id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return repository.findAll();
    }

    @Override
    public Genre create(String title) {
        return repository.insert(Genre.builder()
                .title(title)
                .build());
    }

    @Override
    @Transactional
    public Genre update(String id, String title) {
        Genre existing = getById(id);
        existing.setTitle(title);
        return repository.save(existing);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
