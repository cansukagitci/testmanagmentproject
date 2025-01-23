package com.example.testmanagment.repository;


import com.example.testmanagment.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuetoUserRepository extends JpaRepository<IssuetoUser,Long> {
    Optional<IssuetoUser> findByIssueAndUser(Issues issue, User user);
}
