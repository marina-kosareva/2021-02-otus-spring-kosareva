package ru.otus.library.shell;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.model.Genre;
import ru.otus.library.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GenresCommands {

    private final GenreService service;

    @ShellMethod(value = "Get all genres", key = "getGenres")
    public List<Genre> getGenres() {
        return service.getAll();
    }

    @ShellMethod(value = "Get genre by id", key = "getGenreById")
    public Genre getGenreById(String id) {
        return service.getById(id);
    }

    @ShellMethod(value = "Create genre by title", key = "createGenre")
    public Genre createGenre(String title) {
        return service.create(title);
    }

    @ShellMethod(value = "Update genre's title by id", key = "updateGenre")
    public Genre updateGenre(String id, String title) {
        return service.update(id, title);
    }

    @ShellMethod(value = "Delete genre by id", key = "deleteGenre")
    public String deleteGenre(String id) {
        service.deleteById(id);
        return "successfully deleted";
    }
}
