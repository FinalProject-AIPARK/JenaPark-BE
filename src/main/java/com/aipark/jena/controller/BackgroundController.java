package com.aipark.jena.controller;

import com.aipark.jena.dto.Response;
import com.aipark.jena.service.BackgroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
