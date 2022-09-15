package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestProject;
import com.aipark.jena.dto.Response;
import com.aipark.jena.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/create-tts")
    public ResponseEntity<Response.Body> createTTS(@RequestBody RequestProject.CreateTTS ttsInputDto) {
        return projectService.createTTS(ttsInputDto);
    }
}
