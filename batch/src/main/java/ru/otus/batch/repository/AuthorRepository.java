package ru.otus.batch.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.batch.model.AuthorDocument;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {

    Optional<AuthorDocument> findByFirstNameAndLastName(String firstName, String lastName);
}
