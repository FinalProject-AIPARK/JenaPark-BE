package com.aipark.jena.service;

import org.springframework.http.ResponseEntity;

import static com.aipark.jena.dto.RequestVideo.ChangeTitle;
import static com.aipark.jena.dto.Response.Body;

public interface VideoService {

    ResponseEntity<Body> createVideo(Long projectId);

    ResponseEntity<Body> renameVideo(Long videoId, ChangeTitle changeTitle);

    ResponseEntity<Body> deleteVideo(Long videoId);
}
