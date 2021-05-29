package ru.otus.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.dto.BookDto;
import ru.otus.library.request.CreateBookRequest;
import ru.otus.library.request.UpdateBookRequest;
import ru.otus.library.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/book")
    public List<BookDto> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/book/{id}")
    public BookDto getById(@PathVariable("id") String id) {
        return bookService.getBookDtoById(id);
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto save(@RequestBody @Valid CreateBookRequest createBookRequest) {
        return bookService.create(createBookRequest.getTitle(), createBookRequest.getGenreId(),
                createBookRequest.getAuthorId());
    }

    @PutMapping("/book/{id}")
    public BookDto update(@PathVariable("id") String id, @RequestBody @Valid UpdateBookRequest request) {
        return bookService.update(id, request.getTitle(), request.getVersion());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/book/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        bookService.deleteById(id);
    }

}
