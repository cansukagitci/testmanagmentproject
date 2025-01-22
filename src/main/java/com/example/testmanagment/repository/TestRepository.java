package com.example.testmanagment.repository;

import com.example.testmanagment.model.Project;
import com.example.testmanagment.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TestRepository extends JpaRepository<Test,Long> {

    @Query(value = "SELECT COUNT(*) FROM tests WHERE result = 'Başarılı'", nativeQuery = true)
    long countPassedTests();

    @Query(value = "SELECT COUNT(*) FROM tests WHERE result = 'Başarısız'", nativeQuery = true)
    long countFiledTests();
    @Query(value = "SELECT COUNT(*) FROM tests", nativeQuery = true)
    long countTotalTests();
    Optional<Test> findById(Long id);
    List<Test> findByIsdeleted(boolean isdeleted);
    List<Test> findByIsdeletedFalse();
}
