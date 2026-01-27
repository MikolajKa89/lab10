package com.example.lab10.controller;

import com.example.lab10.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 1. Wyłączamy Security (bramkarza)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // 2. TO JEST NOWOŚĆ: Tworzymy "manekina" zamiast prawdziwego serwisu
    private TaskService taskService;

    @Test
    @WithMockUser(username = "student")
    void shouldAllowAccessForAuthenticatedUser() throws Exception {
        // 3. Uczymy manekina: "Jak ktoś zapyta o zadania, zwróć pustą listę i nie marudź o braku usera w bazie"
        Mockito.when(taskService.getMyTasks()).thenReturn(Collections.emptyList());

        // 4. Wykonujemy test
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }
}
