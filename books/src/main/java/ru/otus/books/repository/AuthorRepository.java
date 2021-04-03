package ru.otus.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
