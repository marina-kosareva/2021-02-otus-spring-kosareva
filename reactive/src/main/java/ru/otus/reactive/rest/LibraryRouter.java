package ru.otus.reactive.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class LibraryRouter {

    @Bean
    public RouterFunction<ServerResponse> bookRouter(BookHandler bookHandler,
                                                     @Value("classpath:/public/index.html") final Resource indexHtml) {

        return route()
                .GET("/", request -> ok().contentType(TEXT_HTML).bodyValue(indexHtml))
                .GET("/book", accept(APPLICATION_JSON), bookHandler::getAll)
                .GET("/book/{id}", accept(APPLICATION_JSON), bookHandler::getById)
                .POST("/book", accept(APPLICATION_JSON), bookHandler::save)
                .PUT("/book/{id}", accept(APPLICATION_JSON), bookHandler::update)
                .DELETE("/book/{id}", accept(APPLICATION_JSON), bookHandler::deleteById)
                .build();
    }
}
