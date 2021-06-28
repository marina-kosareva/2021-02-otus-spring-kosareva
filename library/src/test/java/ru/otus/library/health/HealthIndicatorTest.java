package ru.otus.library.health;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
class HealthIndicatorTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void libraryUserHealth() throws Exception {
        mvc.perform(get("/actuator/health/libraryUser"))
                .andExpect(status().isOk())
                .andExpect(content().json(new JSONObject()
                        .put("status", "UP")
                        .put("details", new JSONObject()
                                .put("message", "count of users: 2")).toString()));
    }
}
