package ru.otus.batch.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.batch.entities.GenreEntity;
import ru.otus.batch.model.GenreDocument;
import ru.otus.batch.repository.GenreRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultGenreService implements GenreService {

    private final GenreRepository repository;

    @Override
    public GenreDocument getOrCreate(GenreEntity entity) {
        return repository.findByTitle(entity.getTitle())
                .orElseGet(() -> repository.save(convertToGenreDocument(entity)));
    }

    private GenreDocument convertToGenreDocument(GenreEntity entity) {
        return GenreDocument.builder()
                .title(entity.getTitle())
                .build();
    }
}
