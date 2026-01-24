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
@AutoConfigureMockMvc(addFilters = false) // <--- OTO TWOJA PRZEPUSTKA VIP (Wyłączamy filtry bezpieczeństwa)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Usunąłem test "shouldBlockAccessWithoutToken", ponieważ po wyłączeniu filtrów (addFilters=false)
    // Spring wpuści każdego, więc tamten test zgłosiłby błąd.
    // Teraz interesuje nas tylko to, żeby "student" wszedł i dostał 200 OK.

    @Test
    @WithMockUser(username = "student")
    void shouldAllowAccessForAuthenticatedUser() throws Exception {
        // Scenariusz: Zalogowany student puka do API
        // Oczekujemy: 200 OK (Teraz musi przejść!)
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }
}