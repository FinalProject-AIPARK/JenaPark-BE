package com.aipark.jena.service;

import com.aipark.jena.dto.RequestAudio;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static com.aipark.jena.dto.Response.Body;

public interface AudioSampleService {
    CompletableFuture<ResponseEntity<Body>> audioSampleList(RequestAudio.AudioSampleDto audioSampleDto);

    CompletableFuture<ResponseEntity<Body>> audioSampleSearch(RequestAudio.AudioSampleSearchDto audioSampleSearchDto);
}
