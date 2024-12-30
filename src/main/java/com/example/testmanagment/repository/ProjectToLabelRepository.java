package com.example.testmanagment.repository;

import com.example.testmanagment.model.ProjecttoLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectToLabelRepository extends JpaRepository<ProjecttoLabel,Long> {
}
