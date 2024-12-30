package com.example.testmanagment.repository;

import com.example.testmanagment.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByName(String name);

}
