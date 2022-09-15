package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Override
    boolean existsById(Long projectId);
}
