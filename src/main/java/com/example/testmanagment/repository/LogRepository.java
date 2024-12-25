package com.example.testmanagment.repository;

import com.example.testmanagment.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntry,Long> {
}
