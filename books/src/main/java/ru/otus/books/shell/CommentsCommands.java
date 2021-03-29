package ru.otus.books.shell;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.books.model.Comment;
import ru.otus.books.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CommentsCommands {
    private final CommentService service;

    @ShellMethod(value = "Get all comments", key = "getComments")
    public List<Comment> getComments() {
        return service.getAll();
    }

    @ShellMethod(value = "Get all comments for book by bookId", key = "getCommentsByBookId")
    public List<Comment> getCommentsByBookId(Long bookId) {
        return service.getByBookId(bookId);
    }

    @ShellMethod(value = "Get comment by id", key = "getCommentById")
    public Comment getCommentById(Long id) {
        return service.getById(id);
    }

    @ShellMethod(value = "Create comment for book", key = "createComment")
    public Comment createComment(String text, Long bookId) {
        return service.create(text, bookId);
    }

    @ShellMethod(value = "Update comment's text by id", key = "updateComment")
    public String updateComment(Long id, String text) {
        return service.update(id, text) == 1
                ? "successfully updated"
                : "not updated: possible the comment with this id does not exist";
    }

    @ShellMethod(value = "Delete comment by id", key = "deleteComment")
    public String deleteComment(Long id) {
        return service.deleteById(id) == 1
                ? "successfully deleted"
                : "not deleted: possible the comment with this id does not exist";
    }
}
