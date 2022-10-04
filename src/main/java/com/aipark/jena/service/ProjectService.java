package com.aipark.jena.service;

import com.aipark.jena.dto.Response.Body;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.*;

public interface ProjectService {
    ResponseEntity<Body> createProject();

    ResponseEntity<Body> inquiryProject(Long projectId);

    ResponseEntity<Body> changeTitle(ChangeTitle titleInputDto);

    ResponseEntity<Body> createTTS(CreateTTS ttsInputDto) throws IOException;

    ResponseEntity<Body> updateTTS(UpdateTTS ttsInputDto);

    ResponseEntity<Body> deleteAudioInfo(Long projectId, Long audioId);

    ResponseEntity<Body> uploadAudio(Long projectId, AudioUploadDto audioUploadDto) throws IOException;

    ResponseEntity<Body> mergeAudio(Long projectId);

    ResponseEntity<Body> deleteUploadAudio(Long projectId);

    ResponseEntity<Body> historyProject();
}
