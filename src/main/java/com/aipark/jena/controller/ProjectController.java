package com.aipark.jena.controller;

import com.aipark.jena.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.*;
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

    @PostMapping("/title")
    public ResponseEntity<Body> changeTitle(@RequestBody ChangeTitle titleInputDto) {
        return projectService.changeTitle(titleInputDto);
    }

    @PostMapping("/create-tts")
    public ResponseEntity<Body> createTTS(@RequestBody CreateTTS ttsInputDto) {
        return projectService.createTTS(ttsInputDto);
    }

    @PostMapping("/update-tts")
    public ResponseEntity<Body> updateTTS(@RequestBody UpdateTTS ttsInputDto) {
        return projectService.updateTTS(ttsInputDto);
    }

    @GetMapping("/{projectId}}/audio")
    public ResponseEntity<Body> mergeAudio(@PathVariable Long projectId) {
        return projectService.mergeAudio(projectId);
    }

    @PostMapping("/{projectId}/audio/upload")
    public ResponseEntity<Body> uploadAudio(@PathVariable Long projectId, @ModelAttribute AudioUploadDto audioUploadDto) throws IOException {
        return projectService.uploadAudio(projectId, audioUploadDto);
    }

    @DeleteMapping("/{projectId}/audio/upload")
    public ResponseEntity<Body> deleteUploadAudio(@PathVariable Long projectId) {
        return projectService.deleteUploadAudio(projectId);
    }
}
