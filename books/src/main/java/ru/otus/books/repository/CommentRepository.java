package ru.otus.books.repository;

import ru.otus.books.model.Comment;

import java.util.List;

public interface CommentRepository {

    Comment getById(Long id);

    List<Comment> getByBookId(Long bookId);

    List<Comment> getAll();

    Comment create(String text, Long bookId);

    int update(Long id, String text);

    int deleteById(Long id);
}
