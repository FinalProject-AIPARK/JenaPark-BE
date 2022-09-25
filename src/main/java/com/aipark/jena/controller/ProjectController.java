package com.aipark.jena.controller;

import com.aipark.jena.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.ChangeTitle;
import static com.aipark.jena.dto.RequestProject.CreateTTS;
import static com.aipark.jena.dto.Response.Body;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@RestController
public class ProjectController {
    private final ProjectServiceImpl projectService;

    @PostMapping
    public ResponseEntity<Body> createProject() {
        return projectService.createProject();
    }

    @PostMapping
    public ResponseEntity<Body> changeTitle(@RequestBody ChangeTitle titleInputDto) {
        return projectService.changeTitle(titleInputDto);
    }

    @PostMapping("/create-tts")
    public ResponseEntity<Body> createTTS(@RequestBody CreateTTS ttsInputDto) {
        return projectService.createTTS(ttsInputDto);
    }

    @PostMapping("/audio/upload")
    public ResponseEntity<Body> uploadAudio(@RequestBody AudioUploadDto audioUploadDto) {
        return projectService.uploadAudio(audioUploadDto);
    }
}
