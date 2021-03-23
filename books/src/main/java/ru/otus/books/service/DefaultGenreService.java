package ru.otus.books.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.books.dao.GenreDao;
import ru.otus.books.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultGenreService implements GenreService {

    private final GenreDao dao;

    @Override
    public Genre getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public List<Genre> getAll() {
        return dao.getAll();
    }

    @Override
    public Long create(String title) {
        return dao.create(Genre.builder()
                .title(title)
                .build());
    }

    @Override
    public int update(Long id, String title) {
        return dao.update(id, title);
    }

    @Override
    public int deleteById(Long id) {
        return dao.deleteById(id);
    }
}
