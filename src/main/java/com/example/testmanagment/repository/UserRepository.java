package com.example.testmanagment.repository;

import com.example.testmanagment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
