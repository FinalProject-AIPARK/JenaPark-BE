package com.aipark.jena.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioSampleRepository extends JpaRepository<AudioSample, Long> {
    List<AudioSample> findAllBySexAndLang(String sex, String lang);
}
