package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.model.LibraryUser;

import java.util.Optional;

public interface UserRepository extends MongoRepository<LibraryUser, String> {

    Optional<LibraryUser> findByName(String userName);
}
