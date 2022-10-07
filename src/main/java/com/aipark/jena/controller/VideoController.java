package com.aipark.jena.controller;

import com.aipark.jena.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aipark.jena.dto.Response.Body;

@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@RestController
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/{projectId}/video")
    public ResponseEntity<Body> createVideo(@PathVariable Long projectId) {
        return videoService.createVideo(projectId);
    }
}
