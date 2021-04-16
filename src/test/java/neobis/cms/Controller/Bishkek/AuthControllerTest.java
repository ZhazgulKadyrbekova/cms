package neobis.cms.Controller.Bishkek;

import com.fasterxml.jackson.databind.ObjectMapper;
import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserDTO;
import neobis.cms.Dto.UserRejectDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class AuthControllerTest {
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
    void createUser() throws Exception {
        UserDTO user = new UserDTO("1804.01026@manas.edu.kg", "management", "bishkek", "Zhazgul",
                "Kadyrbekova", "Kadyrbekovna", "+996 (500) 10 20 30", "12345678");
        String jsonRequest = mapper.writeValueAsString(user);
        MvcResult result = mvc
                    .perform(post("/register/user")
                            .content(jsonRequest)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getListOfUserToConfirm() throws Exception {
        MvcResult result = mvc
                    .perform(get("/register/admin/toConfirm")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void confirmAndSendActivation() throws Exception {
        MvcResult result = mvc
                    .perform(post("/register/admin/confirm/{id}", 3)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void activate() throws Exception {
        String code = "c6cb19cc-26c1-4dbc-b367-e162f2d77401";
        MvcResult result = mvc
                    .perform(get("/register/activate/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getToken() throws Exception {
        UserAuthDTO user = new UserAuthDTO("zhazgul004@gmail.com", "12345678");
        String jsonRequest = mapper.writeValueAsString(user);
        MvcResult result = mvc
                    .perform(post("/register/auth")
                            .content(jsonRequest)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void forgotPassword() throws Exception{
        String email = "zhazgul004@gmail.com";
        MvcResult result = mvc
                    .perform(post("/register/forgotPassword/{email}", email)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void restore() throws Exception {
        String code = "0ebe76b2-199a-4845-8a43-ef06199b0632";
        MvcResult result = mvc
                    .perform(get("/register/restore/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void setPassword() throws Exception {
        UserAuthDTO user = new UserAuthDTO("zhazgul004@gmail.com", "12345678");
        String jsonRequest = mapper.writeValueAsString(user);
        MvcResult result = mvc
                    .perform(post("/register/setPassword")
                            .content(jsonRequest)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void rejectAndSendEmail() throws Exception{
        UserRejectDTO reject = new UserRejectDTO("1804.01026@manas.edu.kg", "description");
        String jsonRequest = mapper.writeValueAsString(reject);
        MvcResult result = mvc
                    .perform(post("/register/admin/confirm/{id}", 4)
                            .content(jsonRequest)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

}