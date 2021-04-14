package ru.otus.library.shell;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.model.Comment;
import ru.otus.library.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CommentsCommands {
    private final CommentService service;

    @ShellMethod(value = "Get all comments by bookId", key = "getCommentsByBookId")
    public List<Comment> getCommentsByBookId(String bookId) {
        return service.getByBookId(bookId);
    }

    @ShellMethod(value = "Create comment for book", key = "createComment")
    public String createComment(String text, String bookId) {
        service.create(text, bookId);
        return "successfully created";
    }

    @ShellMethod(value = "Delete comment by id for book", key = "deleteComment")
    public String deleteComment(String id, String bookId) {
        return service.deleteByIdForBook(id, bookId) != 0
                ? "successfully deleted"
                : "nothing deleted";
    }
}
