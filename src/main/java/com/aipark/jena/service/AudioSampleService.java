package com.aipark.jena.service;

import com.aipark.jena.dto.RequestAudio;
import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface AudioSampleService {
    CompletableFuture<ResponseEntity<Response.Body>> audioSampleList(RequestAudio.AudioSampleDto audioSampleDto);
}
