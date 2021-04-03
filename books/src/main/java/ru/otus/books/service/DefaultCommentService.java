package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.exceptions.CommentRepositoryException;
import ru.otus.books.model.Comment;
import ru.otus.books.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultCommentService implements CommentService {

    private final CommentRepository repository;
    private final BookService bookService;

    @Override
    @Transactional(readOnly = true)
    public Comment getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentRepositoryException("error getting comment by id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getByBookId(Long bookId) {
        return repository.getByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment create(String text, Long bookId) {
        try {
            return repository.saveAndFlush(Comment.builder()
                    .text(text)
                    .book(bookService.getById(bookId))
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new CommentRepositoryException("error during comment creating", ex);
        }
    }

    @Override
    @Transactional
    public Comment update(Long id, String text) {
        try {
            Comment existing = getById(id);
            existing.setText(text);
            return repository.saveAndFlush(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new CommentRepositoryException("error during comment updating", ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
