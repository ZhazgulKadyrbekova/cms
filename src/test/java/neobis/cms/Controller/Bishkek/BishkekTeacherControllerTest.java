package neobis.cms.Controller.Bishkek;

import com.fasterxml.jackson.databind.ObjectMapper;
import neobis.cms.Dto.TeacherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class BishkekTeacherControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired private ObjectMapper mapper;
    private MockMvc mvc;

    String username = "admin@gmail.com";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user(username).roles("ADMIN")))
                .defaultRequest(post("/").with(user(username).roles("ADMIN")))
                .defaultRequest(put("/").with(user(username).roles("ADMIN")))
                .defaultRequest(delete("/").with(user(username).roles("ADMIN")))
                .apply(springSecurity())
                .build();
    }

    @Test
    void getAll() throws Exception {
        MvcResult result = mvc
                .perform(get("/bishkek/worker")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void filter() throws Exception {
        MvcResult result = mvc
                .perform(get("/bishkek/worker/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void search() throws Exception {
        MvcResult result = mvc
                .perform(get("/bishkek/worker/search?field=name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addNewTeacher() throws Exception{
        TeacherDTO teacher = new TeacherDTO("name", "surname", null,
                "aaa@gmail.com", "+996 (500) 12 12 32", 3, 3,
                "1234567890",   LocalDate.now(), LocalDate.now(), "desc");
        String jsonRequest = mapper.writeValueAsString(teacher);
        MvcResult result = mvc
                .perform(post("/bishkek/worker")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateTeacher() throws Exception{
        TeacherDTO teacher = new TeacherDTO("name", "surname", null,
                "aaaa@gmail.com", "+996 (500) 12 12 32", 3, 4,
                "1234567890",   LocalDate.now(), LocalDate.now(), "desc");
        String jsonRequest = mapper.writeValueAsString(teacher);
        MvcResult result = mvc
                .perform(put("/bishkek/worker/{id}", 2)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deleteTeacher() throws Exception {
        MvcResult result = mvc
                .perform(delete("/bishkek/worker/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}