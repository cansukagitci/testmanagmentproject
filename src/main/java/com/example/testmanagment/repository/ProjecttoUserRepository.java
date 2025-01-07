package com.example.testmanagment.repository;


import com.example.testmanagment.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjecttoUserRepository extends JpaRepository<ProjecttoUser,Long> {
    Optional<ProjecttoUser> findByProjectAndUser(Project project, User user);
}
