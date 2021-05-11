package ru.otus.library.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.GenreService;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/")
    public String getIndexView(Model model) {
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("authors", authorService.getAll());
        return "index";
    }

}
