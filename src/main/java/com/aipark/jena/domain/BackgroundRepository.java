package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BackgroundRepository extends JpaRepository<Background, Long> {

    List<Background> findAllByIsUpload(boolean isUpload);

    List<Background> findAllByMember(Member member);

    Optional<Background> findByMember(Member member);
}
