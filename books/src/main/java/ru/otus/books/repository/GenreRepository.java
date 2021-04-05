package ru.otus.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
