package ru.otus.batch.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.batch.model.GenreDocument;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<GenreDocument, String> {

    Optional<GenreDocument> findByTitle(String title);
}
