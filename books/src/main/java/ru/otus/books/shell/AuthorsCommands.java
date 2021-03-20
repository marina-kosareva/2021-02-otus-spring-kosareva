package ru.otus.books.shell;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.books.model.Author;
import ru.otus.books.service.AuthorService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AuthorsCommands {

    private final AuthorService service;

    @ShellMethod(value = "Get all authors", key = "getAuthors")
    public List<Author> getAuthors() {
        return service.getAll();
    }

    @ShellMethod(value = "Get author by id", key = "getAuthorById")
    public Author getAuthorById(Long id) {
        return service.getById(id);
    }

    @ShellMethod(value = "Create author by firstName and lastName", key = "createAuthor")
    public String createAuthor(String firstName, String lastName) {
        Long id = service.create(firstName, lastName);
        return "successfully created with id = " + id;
    }

    @ShellMethod(value = "Update author's firstName, lastName by id", key = "updateAuthor")
    public String updateAuthor(Long id, String firstName, String lastName) {
        return service.update(id, firstName, lastName) == 1
                ? "successfully updated"
                : "not updated: possible the author with this id does not exist";
    }

    @ShellMethod(value = "Delete author by id", key = "deleteAuthor")
    public String deleteAuthor(Long id) {
        return service.deleteById(id) == 1
                ? "successfully deleted"
                : "not deleted: possible the author with this id does not exist";
    }
}
