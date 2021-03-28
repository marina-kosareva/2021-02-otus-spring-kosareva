package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.model.Author;
import ru.otus.books.repository.AuthorRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public Author getById(Long id) {
        return repository.getById(id);
    }

    @Override
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
    public int update(Long id, String firstName, String lastName) {
        return repository.update(id, firstName, lastName);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return repository.deleteById(id);
    }
}
