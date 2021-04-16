package neobis.cms.Controller.Osh;

import com.fasterxml.jackson.databind.ObjectMapper;
import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class OshClientControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired private ObjectMapper mapper;
    @Autowired ResourceLoader resourceLoader;
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
                .perform(get("/osh/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void filter() throws Exception {
        MvcResult result = mvc
                .perform(get("/osh/client/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void search() throws Exception {
        MvcResult result = mvc
                .perform(get("/osh/client/search?dateAfter=2021-04-13 18:00&dateBefore=2021-04-16 18:00&field=name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getFile() throws Exception {
        MvcResult result = mvc
                .perform(get("/osh/client/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getById() throws Exception{
        MvcResult result = mvc
                .perform(get("/osh/client/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void importFile() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:export.xlsx");
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile file = new MockMultipartFile("file", "export.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.multipart("/osh/client/import")
                        .file(file))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getAllByStatus() throws Exception {
        MvcResult result = mvc
                .perform(get("/osh/client/status/{status_id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addClient() throws Exception {
        ClientDTO client = new ClientDTO("+996 (555) 10 20 30", "Adinay", "Satybaldieva",
                "Satybaldievna", "email@gmail.com", 1, 0, 0, true,
                true, 0, "description", LocalDateTime.now(), BigDecimal.ONE, 0, 0);
        String jsonRequest = mapper.writeValueAsString(client);
        MvcResult result = mvc
                .perform(post("/osh/client")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void changeStatus() {
    }

    @Test
    void changeCity() throws Exception {
        MvcResult result = mvc
                .perform(put("/osh/client/{client_id}/city", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateClient() throws Exception {
        ClientDTO client = new ClientDTO("+996 (555) 10 20 30", "Adinay", "Satybaldieva",
                "Satybaldievna", "email@gmail.com", 1, 1, 1, true,
                true, 1, "description", LocalDateTime.now(), BigDecimal.ONE, 1, 1);
        String jsonRequest = mapper.writeValueAsString(client);
        MvcResult result = mvc
                .perform(put("/osh/client/{id}", 1)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deleteClient() throws Exception {
        MvcResult result = mvc
                .perform(delete("/osh/client/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addPayment() throws Exception {
        PaymentDTO payment = new PaymentDTO("April", BigDecimal.valueOf(3200), true, 1);
        String jsonRequest = mapper.writeValueAsString(payment);
        MvcResult result = mvc
                .perform(post("/osh/client/{client_id}/payment", 4)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void editPayment() throws Exception {
        PaymentDTO payment = new PaymentDTO("April", BigDecimal.valueOf(3200), true, 1);
        String jsonRequest = mapper.writeValueAsString(payment);
        MvcResult result = mvc
                .perform(put("/osh/client/{client_id}/payment/{payment_id}", 4, 2)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deletePayment() throws Exception {
        MvcResult result = mvc
                .perform(delete("/osh/client/{client_id}/payment/{payment_id}", 4, 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}