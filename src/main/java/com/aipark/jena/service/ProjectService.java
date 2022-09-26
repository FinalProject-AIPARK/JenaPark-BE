package com.aipark.jena.service;

import com.aipark.jena.dto.Response.Body;
import org.springframework.http.ResponseEntity;

import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.ChangeTitle;
import static com.aipark.jena.dto.RequestProject.CreateTTS;

public interface ProjectService {
    ResponseEntity<Body> createProject();
    ResponseEntity<Body> changeTitle(ChangeTitle titleInputDto);
    ResponseEntity<Body> createTTS(CreateTTS ttsInputDto);
    ResponseEntity<Body> uploadAudio(AudioUploadDto audioUploadDto);
}
