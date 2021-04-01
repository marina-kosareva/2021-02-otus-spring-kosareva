package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return repository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getByBookId(Long bookId) {
        return repository.getByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment create(String text, Long bookId) {
        return repository.create(Comment.builder()
                .text(text)
                .book(bookService.getById(bookId))
                .build());
    }

    @Override
    @Transactional
    public Comment update(Long id, String text) {
        Comment existing = getById(id);
        existing.setText(text);
        return repository.update(existing);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
