package ru.otus.library.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Genre> event) {
        Genre genre = event.getSource();
        bookRepository.findByGenreId(genre.getId())
                .stream()
                .peek(book -> book.setGenre(genre))
                .forEach(bookRepository::save);
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        String id = String.valueOf(event.getSource().get("_id"));
        bookRepository.removeByGenreId(id);
    }

}
