package ru.otus.library.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.CommentsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultCommentService implements CommentService {

    private final CommentsRepository commentsRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Comment> getByBookId(String bookId) {
        return commentsRepository.findCommentsByBookId(bookId);
    }

    @Override
    @Transactional
    public void create(String text, String bookId) {
        commentsRepository.createCommentForBook(new Comment(text), bookId);
    }

    @Override
    public long deleteByIdForBook(String id, String bookId) {
        return commentsRepository.deleteCommentByIdForBook(id, bookId);
    }

}
