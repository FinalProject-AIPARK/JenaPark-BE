package com.aipark.jena.controller;

import com.aipark.jena.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aipark.jena.dto.RequestVideo.ChangeTitle;
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

    @PostMapping("/video/{videoId}")
    public ResponseEntity<Body> renameVideo(@PathVariable Long videoId, @RequestBody ChangeTitle changeTitle) {
        return videoService.renameVideo(videoId, changeTitle);
    }

    @DeleteMapping("/video/{videoId}")
    public ResponseEntity<Body> deleteVideo(@PathVariable Long videoId) {
        return videoService.deleteVideo(videoId);
    }
}
