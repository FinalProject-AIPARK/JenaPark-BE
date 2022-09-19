package com.aipark.jena.service;

import com.aipark.jena.dto.RequestProject;
import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

public interface ProjectService {
    ResponseEntity<Response.Body> createProject();
    ResponseEntity<Response.Body> createTTS(RequestProject.CreateTTS ttsInputDto);
}
