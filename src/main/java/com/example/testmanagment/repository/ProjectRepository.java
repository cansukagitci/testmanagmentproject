package com.example.testmanagment.repository;

import com.example.testmanagment.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {

}
