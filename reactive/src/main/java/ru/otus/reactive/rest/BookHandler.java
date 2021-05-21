package ru.otus.reactive.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.reactive.model.Book;
import ru.otus.reactive.repository.BookRepository;
import ru.otus.reactive.request.CreateBookRequest;
import ru.otus.reactive.request.UpdateBookRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookRepository repository;
    private final RequestHandler requestHandler;

    public Mono<ServerResponse> getById(ServerRequest request) {
        return repository.findById(request.pathVariable("id"))
                .flatMap(book -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(book)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(repository.findAll(), Book.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return requestHandler.requireValidBody(body -> body.map(this::getBook)
                        .flatMap(book -> ok()
                                .contentType(APPLICATION_JSON)
                                .body(repository.save(book), Book.class)),
                request, CreateBookRequest.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        return repository.findById(request.pathVariable("id"))
                .flatMap(repository::delete)
                .then(ok().build())
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(existing ->
                        requestHandler.requireValidBody(
                                body -> body.map(updateBookRequest -> getBook(id, updateBookRequest))
                                        .flatMap(book -> ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(repository.save(book), Book.class)),
                                request, UpdateBookRequest.class)
                )
                .switchIfEmpty(notFound().build());
    }

    private Book getBook(String id, UpdateBookRequest updateBookRequest) {
        return Book.builder()
                .id(id)
                .title(updateBookRequest.getTitle())
                .version(updateBookRequest.getVersion())
                .author(updateBookRequest.getAuthor())
                .genre(updateBookRequest.getGenre())
                .build();
    }

    private Book getBook(CreateBookRequest createBookRequest) {
        return Book.builder()
                .title(createBookRequest.getTitle())
                .author(createBookRequest.getAuthor())
                .genre(createBookRequest.getGenre())
                .build();
    }
}
