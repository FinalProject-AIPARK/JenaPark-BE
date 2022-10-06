package com.aipark.jena.service;

import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

public interface VideoService {

    ResponseEntity<Response.Body> createVideo(Long projectId);
}
