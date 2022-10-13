package com.aipark.jena.service;

import com.aipark.jena.dto.Response.Body;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.aipark.jena.dto.RequestAudio.AudioUploadDto;
import static com.aipark.jena.dto.RequestProject.*;

public interface ProjectService {
    ResponseEntity<Body> createProject();

    ResponseEntity<Body> inquiryProject(Long projectId);

    ResponseEntity<Body> changeTitle(ChangeTitle titleInputDto);

    ResponseEntity<Body> createTTS(CreateTTS ttsInputDto) throws ExecutionException, InterruptedException;

    ResponseEntity<Body> updateTTS(UpdateTTS ttsInputDto) throws ExecutionException, InterruptedException;

    ResponseEntity<Body> deleteAudioInfo(Long projectId, Long audioId);

    ResponseEntity<Body> uploadAudio(Long projectId, AudioUploadDto audioUploadDto) throws IOException;

    ResponseEntity<Body> mergeAudio(Long projectId) throws ExecutionException, InterruptedException;

    ResponseEntity<Body> deleteUploadAudio(Long projectId);

    ResponseEntity<Body> historyProject();

    ResponseEntity<Body> deleteProject(Long projectId);
}
