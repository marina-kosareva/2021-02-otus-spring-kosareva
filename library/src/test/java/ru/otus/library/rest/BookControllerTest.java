package ru.otus.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.mapper.BookMapperImpl;
import ru.otus.library.request.CreateBookRequest;
import ru.otus.library.request.UpdateBookRequest;
import ru.otus.library.service.DefaultAuthorService;
import ru.otus.library.service.DefaultBookService;
import ru.otus.library.service.DefaultGenreService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@Import({DefaultBookService.class, DefaultAuthorService.class, DefaultGenreService.class, BookMapperImpl.class})
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllBooks() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(new JSONArray()
                        .put(new JSONObject()
                                .put("id", "book1Id")
                                .put("version", 0L)
                                .put("title", "book1")
                                .put("genreTitle", "genre1")
                                .put("authorFullName", "firstName1 lastName1"))
                        .put(new JSONObject()
                                .put("id", "book2Id")
                                .put("version", 0L)
                                .put("title", "book2")
                                .put("genreTitle", "genre1")
                                .put("authorFullName", "firstName2 lastName2"))
                        .put(new JSONObject()
                                .put("id", "book3Id")
                                .put("version", 0L)
                                .put("title", "book3")
                                .put("genreTitle", "genre2")
                                .put("authorFullName", "firstName2 lastName2")).toString()
                ));
    }

    @Test
    void getById() throws Exception {
        mvc.perform(get("/book/book3Id"))
                .andExpect(status().isOk())
                .andExpect(content().json((new JSONObject()
                        .put("id", "book3Id")
                        .put("version", 0L)
                        .put("title", "book3")
                        .put("genreTitle", "genre2")
                        .put("authorFullName", "firstName2 lastName2"))
                        .toString()
                ));
    }

    @Test
    void save() throws Exception {
        mvc.perform(post("/book")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateBookRequest("title", "genre1Id", "author2Id"))))
                .andExpect(status().isCreated())
                .andExpect(content().json(new JSONObject()
                        .put("version", 0L)
                        .put("title", "title")
                        .put("genreTitle", "genre1")
                        .put("authorFullName", "firstName2 lastName2")
                        .toString()
                ));
    }

    @Test
    void update() throws Exception {
        mvc.perform(put("/book/book3Id")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UpdateBookRequest("titleNEW", 0L))))
                .andExpect(status().isOk())
                .andExpect(content().json(new JSONObject()
                        .put("id", "book3Id")
                        .put("version", 1L)
                        .put("title", "titleNEW")
                        .put("genreTitle", "genre2")
                        .put("authorFullName", "firstName2 lastName2")
                        .toString()
                ));
    }

    @Test
    void update_optimisticException() throws Exception {
        mvc.perform(put("/book/book3Id")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UpdateBookRequest("titleNEW", 1L))))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof OptimisticLockingFailureException));
    }

    @Test
    void update_unknownId() throws Exception {
        mvc.perform(put("/book/unknownBookId")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UpdateBookRequest("titleNEW", 1L))))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof BookRepositoryException));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/book/book3Id"))
                .andExpect(status().isNoContent());
    }
}
