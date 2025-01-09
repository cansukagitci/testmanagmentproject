package com.example.testmanagment.repository;

import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TestRepository extends JpaRepository<Test,Long> {
    Optional<Test> findById(Long id);
}
