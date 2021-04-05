package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.exceptions.GenreRepositoryException;
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
        try {
            return repository.save(Genre.builder()
                    .title(title)
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new GenreRepositoryException("error during genre creating", ex);
        }
    }

    @Override
    @Transactional
    public Genre update(Long id, String title) {
        try {
            Genre existing = getById(id);
            existing.setTitle(title);
            return repository.saveAndFlush(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new GenreRepositoryException("error during genre updating", ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
