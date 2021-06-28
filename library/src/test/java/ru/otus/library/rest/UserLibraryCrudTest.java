package ru.otus.library.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.model.LibraryUser;
import ru.otus.library.model.Roles;
import ru.otus.library.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserLibraryCrudTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository repository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

    @Test
    @WithMockUser
    void findByName() throws Exception {
        mvc.perform(get("/user/search/findByName?name=admin"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new JSONObject()
                                .put("name", "admin")
                                .put("roles", new JSONArray(asList("ADMIN", "USER")))
                                .put("_links",
                                        new JSONObject()
                                                .put("self", new JSONObject().put("href", "http://localhost/user/60da1f1f20d2d654330f3d8a"))
                                                .put("libraryUser", new JSONObject().put("href", "http://localhost/user/60da1f1f20d2d654330f3d8a")))
                                .toString()
                ));
    }

    @Test
    @WithMockUser
    void save() throws Exception {
        LibraryUser user = LibraryUser.builder()
                .id("someId")
                .name("newUser")
                .password(passwordEncoder.encode("newUserPassword"))
                .roles(Set.of(Roles.USER, Roles.ADMIN))
                .build();
        assertThat(repository.findByName("newUser")).isEmpty();

        mvc.perform(post("/user")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/user/someId"));

        assertThat(repository.findByName("newUser")).hasValue(user);
    }

    @Test
    @WithMockUser
    void update() throws Exception {
        Optional<LibraryUser> current = repository.findById("60da1f1f20d2d654330f3d8a");
        assertThat(current).isNotEmpty();
        assertThat(current.get().getName()).isEqualTo("admin");
        LibraryUser updated = LibraryUser.builder()
                .id(current.get().getId())
                .name("newName")
                .password(current.get().getPassword())
                .roles(current.get().getRoles())
                .build();
        mvc.perform(patch("/user/60da1f1f20d2d654330f3d8a")
                .contentType(APPLICATION_JSON_VALUE)
                .content(new JSONObject().put("name", "newName").toString()));
        Optional<LibraryUser> user = repository.findById("60da1f1f20d2d654330f3d8a");
        assertThat(user).hasValue(updated);
    }

    @Test
    @WithMockUser
    void deleteById() throws Exception {
        Optional<LibraryUser> current = repository.findById("60da1f1f20d2d654330f3d8a");
        assertThat(current).isNotEmpty();

        mvc.perform(delete("/user/60da1f1f20d2d654330f3d8a"))
                .andExpect(status().isNoContent());

        assertThat(repository.findById("60da1f1f20d2d654330f3d8a")).isEmpty();
    }
}
