package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.model.Comment;
import ru.otus.books.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultCommentService implements CommentService {

    private final CommentRepository repository;

    @Override
    public Comment getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Comment> getByBookId(Long bookId) {
        return repository.getByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment create(String text, Long bookId) {
        return repository.create(text, bookId);
    }

    @Override
    @Transactional
    public int update(Long id, String text) {
        return repository.update(id, text);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
