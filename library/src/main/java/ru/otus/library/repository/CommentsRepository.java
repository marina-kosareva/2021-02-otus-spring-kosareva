package ru.otus.library.repository;

import ru.otus.library.model.Comment;

import java.util.List;

public interface CommentsRepository {

    List<Comment> findCommentsByBookId(String id);

    void createCommentForBook(Comment comment, String bookId);

    long deleteCommentByIdForBook(String id, String bookId);
}
