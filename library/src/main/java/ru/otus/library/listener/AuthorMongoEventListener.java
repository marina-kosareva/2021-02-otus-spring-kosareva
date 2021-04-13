package ru.otus.library.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Author;
import ru.otus.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Author> event) {
        Author author = event.getSource();
        bookRepository.findByAuthorId(author.getId())
                .stream()
                .peek(book -> book.setAuthor(author))
                .forEach(bookRepository::save);
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        String id = String.valueOf(event.getSource().get("_id"));
        bookRepository.removeByAuthorId(id);
    }
}
