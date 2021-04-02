package ru.otus.books.repository;

import ru.otus.books.model.Comment;

import java.util.List;

public interface CommentRepository {

    Comment getById(Long id);

    List<Comment> getByBookId(Long bookId);

    List<Comment> getAll();

    Comment create(Comment comment);

    Comment update(Comment comment);

    int deleteById(Long id);
}
