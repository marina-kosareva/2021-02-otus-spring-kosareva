package ru.otus.library.service;

import ru.otus.library.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getByBookId(String id);

    void create(String text, String bookId);

    long deleteByIdForBook(String id, String bookId);
}
