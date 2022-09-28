package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BackgroundRepository extends JpaRepository<Background, Long> {

    List<Background> findAllByIsUpload(boolean isUpload);
}
