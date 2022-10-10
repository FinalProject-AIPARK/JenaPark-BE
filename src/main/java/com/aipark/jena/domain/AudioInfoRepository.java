package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioInfoRepository extends JpaRepository<AudioInfo, Long> {
    void deleteAllByProject(Project project);

    boolean existsByIdAndProject(Long audioID, Project project);
}
