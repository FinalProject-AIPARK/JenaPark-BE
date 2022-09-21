package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestAudio;
import com.aipark.jena.dto.Response;
import com.aipark.jena.service.AudioSampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/audio/sample")
@RestController
public class AudioSampleController {
    private final AudioSampleService audioSampleService;

    @GetMapping
    public ResponseEntity<Response.Body> audioSampleList(@RequestBody RequestAudio.AudioSampleDto audioSampleDto){
        return audioSampleService.audioSampleList(audioSampleDto);
    }
}
