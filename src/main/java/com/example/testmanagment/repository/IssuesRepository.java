package com.example.testmanagment.repository;

import com.example.testmanagment.model.Issues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuesRepository extends JpaRepository<Issues,Long> {
    Optional<Issues> findById(Long id);
}

