package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.books.exceptions.AuthorRepositoryException;
import ru.otus.books.model.Author;
import ru.otus.books.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Author getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AuthorRepositoryException("error getting author by id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return repository.findAll();
    }

    @Override
    public Author create(String firstName, String lastName) {
        try {
            return repository.save(Author.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new AuthorRepositoryException("error during author creating", ex);
        }
    }

    @Override
    @Transactional
    public Author update(Long id, String firstName, String lastName) {
        try {
            Author existing = getById(id);
            existing.setFirstName(firstName);
            existing.setLastName(lastName);
            return repository.saveAndFlush(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new AuthorRepositoryException("error during author updating", ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
