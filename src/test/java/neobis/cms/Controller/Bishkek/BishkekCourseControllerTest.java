package neobis.cms.Controller.Bishkek;

import com.fasterxml.jackson.databind.ObjectMapper;
import neobis.cms.Dto.CoursesDTO;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class BishkekCourseControllerTest {
    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper mapper;
    private MockMvc mvc;

    String username = "admin@gmail.com";

    @BeforeEach
    public void setUp() {
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
                .perform(get("/bishkek/course")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getById() throws Exception{
        MvcResult result = mvc
                .perform(get("/bishkek/course/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getByName() throws Exception{
        MvcResult result = mvc
                .perform(get("/bishkek/course/name/{name}", "Python")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addNewCourse() throws Exception{
        CoursesDTO course = new CoursesDTO("Python", BigDecimal.valueOf(1234), 0);
        String jsonRequest = mapper.writeValueAsString(course);
        MvcResult result = mvc
                .perform(post("/bishkek/course")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateCourse() throws Exception{
        CoursesDTO course = new CoursesDTO("Java", BigDecimal.valueOf(1234), 0);
        String jsonRequest = mapper.writeValueAsString(course);
        MvcResult result = mvc
                .perform(put("/bishkek/course/{id}", 1)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deleteCourse() throws Exception {
        MvcResult result = mvc
                .perform(delete("/bishkek/course/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}