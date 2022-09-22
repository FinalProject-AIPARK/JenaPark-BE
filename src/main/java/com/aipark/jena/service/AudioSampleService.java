package com.aipark.jena.service;

import com.aipark.jena.dto.RequestAudio;
import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

public interface AudioSampleService {
    ResponseEntity<Response.Body> audioSampleList(RequestAudio.AudioSampleDto audioSampleDto);
}
