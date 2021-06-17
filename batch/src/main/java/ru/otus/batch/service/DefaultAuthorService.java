package ru.otus.batch.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.batch.entities.AuthorEntity;
import ru.otus.batch.model.AuthorDocument;
import ru.otus.batch.repository.AuthorRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepository repository;

    @Override
    @Cacheable(value = "authors", key = "#entity.id")
    public AuthorDocument getOrCreate(AuthorEntity entity) {
        return repository.findByFirstNameAndLastName(entity.getFirstName(), entity.getLastName())
                .orElseGet(() -> repository.save(convertToAuthorDocument(entity)));
    }

    private AuthorDocument convertToAuthorDocument(AuthorEntity entity) {
        return AuthorDocument.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }
}
