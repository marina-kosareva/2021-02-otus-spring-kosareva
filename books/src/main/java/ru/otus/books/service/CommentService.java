package ru.otus.books.service;

import ru.otus.books.model.Comment;

import java.util.List;

public interface CommentService {

    Comment getById(Long id);

    List<Comment> getByBookId(Long id);

    List<Comment> getAll();

    Comment create(String text, Long bookId);

    int update(Long id, String text);

    int deleteById(Long id);
}
