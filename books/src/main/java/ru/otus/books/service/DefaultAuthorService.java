package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return repository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public Author create(String firstName, String lastName) {
        return repository.create(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build());
    }

    @Override
    @Transactional
    public Author update(Long id, String firstName, String lastName) {
        Author existing = getById(id);
        existing.setFirstName(firstName);
        existing.setLastName(lastName);
        return repository.update(existing);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
