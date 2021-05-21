package ru.otus.reactive.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.unprocessableEntity;

@Component
@RequiredArgsConstructor
public class RequestHandler {

    private final Validator validator;

    public <T> Mono<ServerResponse> requireValidBody(
            Function<Mono<T>, Mono<ServerResponse>> block,
            ServerRequest request, Class<T> bodyClass) {

        return request
                .bodyToMono(bodyClass)
                .flatMap(
                        body -> validator.validate(body).isEmpty()
                                ? block.apply(Mono.just(body))
                                : unprocessableEntity().bodyValue("validation error")
                );
    }
}
