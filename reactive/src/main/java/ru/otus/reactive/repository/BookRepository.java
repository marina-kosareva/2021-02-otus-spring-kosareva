package ru.otus.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.reactive.model.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
