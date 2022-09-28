package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestBackground;
import com.aipark.jena.dto.Response;
import com.aipark.jena.service.BackgroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/background")
public class BackgroundController {

    private final BackgroundService backgroundService;


    // 배경 리스트
    @GetMapping
    public ResponseEntity<Response.Body> backgroundList(){
        return backgroundService.backgroundList();
    }

    // 배경 선택
    @GetMapping("/{backgroundId}")
    public ResponseEntity<Response.Body> backgroundSelect(@PathVariable Long backgroundId){
        return backgroundService.backgroundSelect(backgroundId);
    }

    // 배경 업로드
    @PostMapping("/upload")
    public ResponseEntity<Response.Body> uploadBackground(@ModelAttribute RequestBackground.BackgroundUploadDto backgroundUploadDto) throws IOException {
        return backgroundService.backgroundUpload(backgroundUploadDto);
    }
}
