package com.geupjo.koreantiger.repository;

import com.geupjo.koreantiger.entity.EducationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationHistoryRepository extends JpaRepository<EducationHistory, Long> {
}
