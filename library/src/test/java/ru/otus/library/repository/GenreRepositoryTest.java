package ru.otus.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.library.listener.GenreMongoEventListener;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
@Import({GenreMongoEventListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreRepositoryTest {

    private static Author AUTHOR = Author.builder()
            .id("author1Id")
            .firstName("firstName1")
            .lastName("lastName1")
            .build();
    private static Genre GENRE = Genre.builder()
            .id("genre1Id")
            .title("genre1")
            .build();
    private static Book BOOK = Book.builder()
            .version(0L)
            .id("book1Id")
            .author(AUTHOR)
            .genre(GENRE)
            .title("book1")
            .build();

    @Autowired
    private GenreRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void shouldDeleteBookIfGenreIsDeleted() {
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(GENRE.getId())), Genre.class))
                .isNotNull();
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class))
                .isNotNull();

        repository.deleteById(GENRE.getId());

        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(GENRE.getId())), Genre.class))
                .isNull();
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is("book1Id")), Book.class))
                .isNull();
    }

    @Test
    void shouldUpdateBookIfGenreIsUpdated() {
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(GENRE.getId())), Genre.class))
                .isEqualTo(GENRE);
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(BOOK.getId())), Book.class))
                .isEqualTo(BOOK);

        Genre genreUpdated = Genre.builder()
                .id(GENRE.getId())
                .title("titleUpdated")
                .build();
        Book bookUpdated = Book.builder()
                .version(BOOK.getVersion() + 1)
                .id(BOOK.getId())
                .author(BOOK.getAuthor())
                .genre(genreUpdated)
                .title(BOOK.getTitle())
                .build();
        repository.save(genreUpdated);

        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(GENRE.getId())), Genre.class))
                .isEqualTo(genreUpdated);
        assertThat(mongoOperations.findOne(Query.query(Criteria.where("_id").is(BOOK.getId())), Book.class))
                .isEqualTo(bookUpdated);
    }
}
