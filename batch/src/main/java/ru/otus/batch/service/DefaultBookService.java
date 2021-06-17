package ru.otus.batch.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.batch.entities.BookEntity;
import ru.otus.batch.model.BookDocument;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultBookService implements BookService {

    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public BookDocument convert(BookEntity entity) {
        return BookDocument.builder()
                .author(authorService.getOrCreate(entity.getAuthor()))
                .genre(genreService.getOrCreate(entity.getGenre()))
                .title(entity.getTitle())
                .build();
    }
}
