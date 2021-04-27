package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.library.dto.BookDto;
import ru.otus.library.request.CreateBookRequest;
import ru.otus.library.request.UpdateBookRequest;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String REDIRECT_TO_INDEX = "redirect:/";

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/")
    public String getIndexView(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "index";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("createBookRequest") CreateBookRequest createBookRequest,
                      BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/add";
        }
        bookService.create(createBookRequest.getTitle(), createBookRequest.getGenreId(), createBookRequest.getAuthorId());
        return REDIRECT_TO_INDEX;
    }

    @GetMapping("/add")
    public String getAddView(Model model) {
        model.addAttribute("createBookRequest", new CreateBookRequest());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("authors", authorService.getAll());
        return "add-book";
    }

    @GetMapping("/update/{id}")
    public String getUpdateView(@PathVariable("id") String id, Model model) {
        BookDto book = bookService.getBookDtoById(id);
        model.addAttribute("book", book);
        model.addAttribute("updateBookRequest", new UpdateBookRequest(book.getTitle()));
        return "update-book";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute("updateBookRequest") UpdateBookRequest request,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/update/" + id;
        }
        bookService.update(id, request.getTitle());
        return REDIRECT_TO_INDEX;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        bookService.deleteById(id);
        return REDIRECT_TO_INDEX;
    }
}
