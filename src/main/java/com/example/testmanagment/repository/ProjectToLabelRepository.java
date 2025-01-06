package com.example.testmanagment.repository;

import com.example.testmanagment.model.Label;
import com.example.testmanagment.model.Project;

import com.example.testmanagment.model.ProjecttoLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectToLabelRepository extends JpaRepository<ProjecttoLabel,Long> {
    Optional<ProjecttoLabel> findByProjectAndLabel(Project project, Label label);



}
