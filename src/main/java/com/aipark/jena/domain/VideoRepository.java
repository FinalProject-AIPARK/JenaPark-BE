package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    boolean existsByIdAndMember(Long videoId, Member member);
}
