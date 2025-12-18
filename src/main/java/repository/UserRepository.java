package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tu Spring automatycznie stworzy zapytanie SQL na podstawie nazwy metody
    User findByEmail(String email);
}