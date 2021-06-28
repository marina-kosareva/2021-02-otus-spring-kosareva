package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.library.model.LibraryUser;

import java.util.Optional;

@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<LibraryUser, String> {

    //http://localhost:8080/user/search/findByName?name=admin
    Optional<LibraryUser> findByName(@Param("name") String userName);
}
