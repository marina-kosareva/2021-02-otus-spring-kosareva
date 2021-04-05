package ru.otus.books.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.books.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"book"})
    List<Comment> getByBookId(Long bookId);

    @EntityGraph(attributePaths = {"book"})
    List<Comment> findAll();

    @EntityGraph(attributePaths = {"book"})
    Optional<Comment> findById(Long id);
}
