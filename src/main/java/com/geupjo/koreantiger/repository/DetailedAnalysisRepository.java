package com.geupjo.koreantiger.repository;

import com.geupjo.koreantiger.entity.DetailedAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailedAnalysisRepository extends JpaRepository<DetailedAnalysis, Long> {
}
