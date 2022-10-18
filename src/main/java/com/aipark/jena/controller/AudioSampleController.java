package com.aipark.jena.controller;

import com.aipark.jena.service.AudioSampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

import static com.aipark.jena.dto.RequestAudio.AudioSampleDto;
import static com.aipark.jena.dto.RequestAudio.AudioSampleSearchDto;
import static com.aipark.jena.dto.Response.Body;

@RequiredArgsConstructor
@RequestMapping("/api/v1/audio/sample")
@RestController
public class AudioSampleController {
    private final AudioSampleService audioSampleService;

    @PostMapping
    public ResponseEntity<Body> audioSampleList(@RequestBody AudioSampleDto audioSampleDto) throws ExecutionException, InterruptedException {
        return audioSampleService.audioSampleList(audioSampleDto).get();
    }

    @PostMapping("/search")
    public ResponseEntity<Body> audioSampleSearch(@RequestBody AudioSampleSearchDto audioSampleSearchDto) throws ExecutionException, InterruptedException {
        return audioSampleService.audioSampleSearch(audioSampleSearchDto).get();
    }
}
