package com.aipark.jena.service;

import com.aipark.jena.dto.RequestBackground;
import com.aipark.jena.dto.Response;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BackgroundService {
    // 배경 업로드
    ResponseEntity<Response.Body> backgroundUpload(RequestBackground.BackgroundUploadDto backgroundUploadDto)throws IOException;

    // 배경 선택
    ResponseEntity<Response.Body> backgroundSelect(Long bgId);

    // 배경 리스트 보여주기
    ResponseEntity<Response.Body> backgroundList();
}
