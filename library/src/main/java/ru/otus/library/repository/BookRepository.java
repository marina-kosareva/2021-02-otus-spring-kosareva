package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    void removeByAuthorId(String authorId);

    void removeByGenreId(String genreId);

    List<Book> findByAuthorId(String authorId);

    List<Book> findByGenreId(String genreId);
}
