package com.example.lab10.repository;

import com.example.lab10.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Standardowa metoda Spring Data JPA
    List<Task> findAllByUser_Email(String email);

    // WYMÓG Z PDF: Surowe zapytanie SQL (Native Query)
    // Pobiera zadania użytkownika po jego ID
    @Query(value = "SELECT * FROM tasks WHERE user_id = :userId", nativeQuery = true)
    List<Task> findTasksByUserIdNative(@Param("userId") Long userId);
}