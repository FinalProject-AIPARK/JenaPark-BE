package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestProject;
import com.aipark.jena.dto.Response;
import com.aipark.jena.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@RestController
public class ProjectController {
    private final ProjectServiceImpl projectService;

    @PostMapping
    public ResponseEntity<Response.Body> createProject() {
        return projectService.createProject();
    }

    @PostMapping("/create-tts")
    public ResponseEntity<Response.Body> createTTS(@RequestBody RequestProject.CreateTTS ttsInputDto) {
        return projectService.createTTS(ttsInputDto);
    }
}
