package ru.otus.reactive.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.reactive.model.Book;
import ru.otus.reactive.repository.BookRepository;
import ru.otus.reactive.request.CreateBookRequest;
import ru.otus.reactive.request.UpdateBookRequest;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {LibraryRouter.class, BookHandler.class, RequestHandler.class})
class BookRouterTest {

    private static final Book BOOK_1 = Book.builder()
            .id("id1")
            .title("title1")
            .author("author1")
            .genre("genre1")
            .build();
    private static final Book BOOK_2 = Book.builder()
            .id("id2")
            .title("title2")
            .author("author2")
            .genre("genre2")
            .build();
    @Autowired
    private RouterFunction<ServerResponse> route;

    @MockBean
    private BookRepository repository;

    @Test
    void getAllBooks() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(asList(BOOK_1, BOOK_2)));
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        client.get()
                .uri("/book")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .isEqualTo(asList(BOOK_1, BOOK_2));
        verify(repository).findAll();
    }

    @Test
    void getById() {
        when(repository.findById("id1")).thenReturn(Mono.just(BOOK_1));
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        client.get()
                .uri("/book/id1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(BOOK_1);
        verify(repository).findById("id1");
    }

    @Test
    void getById_notFound() {
        when(repository.findById("id1")).thenReturn(Mono.empty());
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        client.get()
                .uri("/book/id1")
                .exchange()
                .expectStatus()
                .isNotFound();
        verify(repository).findById("id1");
    }

    @Test
    void save() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        Book book = Book.builder()
                .title("title")
                .author("author")
                .genre("genre")
                .build();
        Book expected = Book.builder()
                .id("id1")
                .version(0L)
                .title("title")
                .author("author")
                .genre("genre")
                .build();
        when(repository.save(book)).thenReturn(Mono.just(expected));

        client.post()
                .uri("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateBookRequest("title", "genre", "author"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(expected);
        verify(repository).save(book);
    }

    @Test
    void save_validationError() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        client.post()
                .uri("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateBookRequest("title", "", ""))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        verifyNoInteractions(repository);
    }

    @Test
    void update() {
        UpdateBookRequest updateRequest = new UpdateBookRequest(0L);
        updateRequest.setTitle("newTitle");
        updateRequest.setGenre("genre");
        updateRequest.setAuthor("author");

        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        Book existing = Book.builder()
                .id("id1")
                .version(0L)
                .title("title")
                .author("author")
                .genre("genre")
                .build();
        Book expected = Book.builder()
                .id("id1")
                .version(1L)
                .title("newTitle")
                .author("author")
                .genre("genre")
                .build();
        Book bookForUpdate = Book.builder()
                .id("id1")
                .version(0L)
                .title("newTitle")
                .author("author")
                .genre("genre")
                .build();
        when(repository.findById("id1")).thenReturn(Mono.just(existing));
        when(repository.save(bookForUpdate)).thenReturn(Mono.just(expected));

        client.put()
                .uri("/book/id1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .isEqualTo(expected);
        verify(repository).findById("id1");
        verify(repository).save(bookForUpdate);
    }

    @Test
    void deleteById() {
        Book existing = Book.builder()
                .id("id1")
                .version(0L)
                .title("title")
                .author("author")
                .genre("genre")
                .build();
        when(repository.findById("id1")).thenReturn(Mono.just(existing));
        when(repository.delete(existing)).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();
        client.delete()
                .uri("/book/id1")
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository).findById("id1");
        verify(repository).delete(existing);
    }
}
