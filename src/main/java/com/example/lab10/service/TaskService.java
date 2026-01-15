package com.example.lab10.service;

import com.example.lab10.model.Task;
import com.example.lab10.model.User;
import com.example.lab10.repository.TaskRepository;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Metoda pomocnicza: Pobiera aktualnie zalogowanego użytkownika
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // ZMIANA TUTAJ: Szukamy po username, nie po emailu!
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));
    }

    public List<Task> getMyTasks() {
        User user = getCurrentUser();
        // Używamy naszej metody z natywnym SQL (wymóg PDF)
        return taskRepository.findTasksByUserIdNative(user.getId());
    }

    public Task addTask(Task task) {
        User user = getCurrentUser();
        task.setUser(user); // Przypisujemy zadanie do zalogowanego użytkownika
        return taskRepository.save(task);
    }
}