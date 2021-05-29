package ru.otus.library.page;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.model.Author;
import ru.otus.library.model.Genre;
import ru.otus.library.service.DefaultAuthorService;
import ru.otus.library.service.DefaultGenreService;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@Import({DefaultAuthorService.class, DefaultGenreService.class})
@WebMvcTest(controllers = PageController.class)
@AutoConfigureDataMongo
class PageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void getIndexView() throws Exception {
        Genre genre1 = Genre.builder()
                .id("genre1Id")
                .title("genre1")
                .build();
        Genre genre2 = Genre.builder()
                .id("genre2Id")
                .title("genre2")
                .build();
        Author author1 = Author.builder()
                .id("author1Id")
                .firstName("firstName1")
                .lastName("lastName1")
                .build();
        Author author2 = Author.builder()
                .id("author2Id")
                .firstName("firstName2")
                .lastName("lastName2")
                .build();
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("genres", hasSize(2)))
                .andExpect(model().attribute("genres", containsInAnyOrder(genre2, genre1)))
                .andExpect(model().attribute("authors", hasSize(2)))
                .andExpect(model().attribute("authors", containsInAnyOrder(author1, author2)));
    }
}
