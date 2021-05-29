package ru.otus.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.dto.BookDto;
import ru.otus.library.exceptions.BookRepositoryException;
import ru.otus.library.request.CreateBookRequest;
import ru.otus.library.request.UpdateBookRequest;
import ru.otus.library.service.BookService;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@AutoConfigureDataMongo
class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookService service;

    @Test
    @WithMockUser
    void getAllBooks() throws Exception {
        when(service.getAll()).thenReturn(asList(BookDto.builder()
                        .id("book1Id")
                        .version(0L)
                        .title("book1")
                        .authorFullName("firstName1 lastName1")
                        .genreTitle("genre1")
                        .build(),
                BookDto.builder()
                        .id("book2Id")
                        .version(0L)
                        .title("book2")
                        .authorFullName("firstName2 lastName2")
                        .genreTitle("genre2")
                        .build()));

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
                                .put("genreTitle", "genre2")
                                .put("authorFullName", "firstName2 lastName2")).toString()
                ));

        verify(service).getAll();
    }

    @Test
    @WithMockUser
    void getById() throws Exception {
        when(service.getBookDtoById("book3Id")).thenReturn(BookDto.builder()
                .id("book3Id")
                .version(0L)
                .title("book3")
                .authorFullName("firstName2 lastName2")
                .genreTitle("genre2")
                .build());
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
        verify(service).getBookDtoById("book3Id");
    }

    @Test
    @WithMockUser
    void save() throws Exception {
        when(service.create("title", "genre1Id", "author2Id"))
                .thenReturn(BookDto.builder()
                        .id("bookId")
                        .version(0L)
                        .title("title")
                        .authorFullName("firstName2 lastName2")
                        .genreTitle("genre1")
                        .build());

        mvc.perform(post("/book")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateBookRequest("title", "genre1Id", "author2Id"))))
                .andExpect(status().isCreated())
                .andExpect(content().json(new JSONObject()
                        .put("id", "bookId")
                        .put("version", 0L)
                        .put("title", "title")
                        .put("genreTitle", "genre1")
                        .put("authorFullName", "firstName2 lastName2")
                        .toString()
                ));

        verify(service).create("title", "genre1Id", "author2Id");
    }

    @Test
    @WithMockUser
    void update() throws Exception {
        when(service.update("book3Id", "titleNEW", 0L))
                .thenReturn(BookDto.builder()
                        .id("book3Id")
                        .version(1L)
                        .title("titleNEW")
                        .authorFullName("firstName2 lastName2")
                        .genreTitle("genre2")
                        .build());
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

        verify(service).update("book3Id", "titleNEW", 0L);
    }

    @Test
    @WithMockUser
    void update_unknownId() throws Exception {
        when(service.update("unknownBookId", "titleNEW", 1L))
                .thenThrow(new BookRepositoryException("message"));
        mvc.perform(put("/book/unknownBookId")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UpdateBookRequest("titleNEW", 1L))))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof BookRepositoryException));
    }

    @Test
    void deleteById_redirectionToLogin() throws Exception {
        mvc.perform(delete("/book/book3Id"))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser
    void deleteById_forbidden() throws Exception {
        mvc.perform(delete("/book/book3Id"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteById_allowed() throws Exception {
        mvc.perform(delete("/book/book3Id"))
                .andExpect(status().isNoContent());

        verify(service).deleteById("book3Id");
    }
}
