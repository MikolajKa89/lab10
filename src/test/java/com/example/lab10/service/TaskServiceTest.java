package com.example.lab10.service;

import com.example.lab10.model.Task;
import com.example.lab10.model.User;
import com.example.lab10.repository.TaskRepository;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Używamy Mockito
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository; // "Udawane" repozytorium zadań

    @Mock
    private UserRepository userRepository; // "Udawane" repozytorium użytkowników

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskService taskService; // To testujemy (prawdziwa klasa)

    private User mockUser;

    @BeforeEach
    void setUp() {
        // Przygotujmy "udawanego" użytkownika do każdego testu
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@test.com");

        // Symulacja SecurityContext (żeby SecurityContextHolder.getContext() nie zwrócił null)
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldAddTaskSuccessfully() {
        // 1. GIVEN (Przygotowanie)
        Task taskToAdd = new Task();
        taskToAdd.setTitle("Test Task");

        // Kiedy serwis zapyta "kto jest zalogowany?", odpowiedz "testuser"
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        // Kiedy serwis poszuka usera w bazie, zwróć naszego mockUsera
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        // Kiedy serwis spróbuje zapisać taska, zwróć tego samego taska
        when(taskRepository.save(any(Task.class))).thenReturn(taskToAdd);

        // 2. WHEN (Akcja)
        Task result = taskService.addTask(taskToAdd);

        // 3. THEN (Weryfikacja)
        assertNotNull(result); // Wynik nie może być pusty
        assertEquals("Test Task", result.getTitle()); // Tytuł musi się zgadzać
        assertEquals(mockUser, result.getUser()); // Task musi być przypisany do usera

        // Sprawdź, czy repository.save() zostało wywołane dokładnie raz
        verify(taskRepository, times(1)).save(taskToAdd);
    }

    @Test
    void shouldGetMyTasks() {
        // 1. GIVEN
        Task t1 = new Task(); t1.setTitle("Task 1");
        Task t2 = new Task(); t2.setTitle("Task 2");
        List<Task> mockTasks = List.of(t1, t2);

        // Mockowanie usera (tak samo jak wyżej)
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Kiedy serwis zapyta o zadania dla ID=1, zwróć listę mockTasks
        when(taskRepository.findTasksByUserIdNative(1L)).thenReturn(mockTasks);

        // 2. WHEN
        List<Task> result = taskService.getMyTasks();

        // 3. THEN
        assertEquals(2, result.size()); // Musi zwrócić 2 zadania
        verify(taskRepository, times(1)).findTasksByUserIdNative(1L);
    }
}