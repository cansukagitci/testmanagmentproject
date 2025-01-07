package com.example.testmanagment.repository;

import com.example.testmanagment.model.Label;
import com.example.testmanagment.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {
    Label findByLabelname(String labelname);
    Label findLabelById(Long id);
    Optional<Label> findById(Long id);
}
