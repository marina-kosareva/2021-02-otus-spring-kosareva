package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.AuthorDao;
import ru.otus.books.model.Author;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthorService implements AuthorService {

    private final AuthorDao dao;

    @Override
    public Author getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public List<Author> getAll() {
        return dao.getAll();
    }

    @Override
    public Long create(String firstName, String lastName) {
        return dao.create(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build());
    }

    @Override
    public int update(Long id, String firstName, String lastName) {
        return dao.update(id, firstName, lastName);
    }

    @Override
    public int deleteById(Long id) {
        return dao.deleteById(id);
    }
}
