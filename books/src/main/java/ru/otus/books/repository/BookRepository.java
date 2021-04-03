package ru.otus.books.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"genre", "author"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"genre", "author"})
    List<Book> findAll();
}
