package com.example.testmanagment.repository;

import com.example.testmanagment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    //List<User> findByIsDeletedFalse();
    User findByEmail(String email);
}
