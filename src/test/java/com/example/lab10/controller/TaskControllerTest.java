package com.example.lab10.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc; // Narzędzie do wysyłania "udawanych" zapytań HTTP

    @Test
    void shouldBlockAccessWithoutToken() throws Exception {
        // Scenariusz: Ktoś puka do API bez tokena
        // Oczekujemy: 403 Forbidden (Odmowa dostępu)
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student") // Magia: Spring "udaje", że ten user jest zalogowany
    void shouldAllowAccessForAuthenticatedUser() throws Exception {
        // Scenariusz: Zalogowany student puka do API
        // Oczekujemy: 200 OK
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }
}
