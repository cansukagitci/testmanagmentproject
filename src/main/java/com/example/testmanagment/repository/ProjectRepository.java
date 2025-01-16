package com.example.testmanagment.repository;

import com.example.testmanagment.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByName(String name);
    Optional<Project> findById(Long id);
    List<Project> findByIsdeleted(boolean isdeleted);
    List<Project> findByIsdeletedFalse();
}
