package ru.otus.library.listener;

import com.google.common.collect.ImmutableMap;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Author;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class CascadeMongoEventListener extends AbstractMongoEventListener<Object> {

    private final BookRepository bookRepository;
    private final Map<String, Consumer<String>> removeFunctionsByCollectionNames;

    public CascadeMongoEventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.removeFunctionsByCollectionNames = ImmutableMap.of(
                "authors", bookRepository::removeByAuthorId,
                "genres", bookRepository::removeByGenreId);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        if (event.getSource() instanceof Author) {
            Author author = (Author) event.getSource();
            bookRepository.findByAuthorId(author.getId())
                    .stream()
                    .peek(book -> book.setAuthor(author))
                    .forEach(bookRepository::save);
        } else if (event.getSource() instanceof Genre) {
            Genre genre = (Genre) event.getSource();
            bookRepository.findByGenreId(genre.getId())
                    .stream()
                    .peek(book -> book.setGenre(genre))
                    .forEach(bookRepository::save);
        }
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        String id = String.valueOf(event.getSource().get("_id"));
        removeFunctionsByCollectionNames.getOrDefault(event.getCollectionName(), any -> {
        }).accept(id);
    }

}
