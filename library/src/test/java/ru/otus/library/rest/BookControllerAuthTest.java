package ru.otus.library.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.mapper.BookMapperImpl;
import ru.otus.library.service.DefaultAuthorService;
import ru.otus.library.service.DefaultBookService;
import ru.otus.library.service.DefaultGenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@Import({DefaultBookService.class, DefaultAuthorService.class, DefaultGenreService.class, BookMapperImpl.class})
@AutoConfigureDataMongo
class BookControllerAuthTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = {"UNKNOWN"})
    void getAllBooks_forbidden() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllBooks_redirect() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser(roles = {"UNKNOWN"})
    void getById_forbidden() throws Exception {
        mvc.perform(get("/book/book3Id"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getById_redirect() throws Exception {
        mvc.perform(get("/book/book3Id"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteById_redirectionToLogin() throws Exception {
        mvc.perform(delete("/book/book1Id"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void deleteById_forbidden() throws Exception {
        mvc.perform(delete("/book/book1Id"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteById_allowed() throws Exception {
        mvc.perform(delete("/book/book2Id"))
                .andExpect(status().isNoContent());
    }
}
