package ru.otus.library.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.exceptions.AuthorRepositoryException;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Author getById(String id) {
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
        return repository.insert(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build());
    }

    @Override
    @Transactional
    public Author update(String id, String firstName, String lastName) {
        Author existing = getById(id);
        existing.setFirstName(firstName);
        existing.setLastName(lastName);
        return repository.save(existing);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
