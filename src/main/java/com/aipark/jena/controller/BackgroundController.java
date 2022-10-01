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
@RequestMapping("/api/v1/projects/")
public class BackgroundController {

    private final BackgroundService backgroundService;


    // 배경 리스트
    @GetMapping("/background")
    public ResponseEntity<Response.Body> backgroundList(Long memberId){// 헤더에 jwt토큰으로 멤버아이디 찾고싶음
        return backgroundService.backgroundList(memberId);
    }

    // 배경 선택
    @GetMapping("/background/{backgroundId}")
    public ResponseEntity<Response.Body> backgroundSelect(@PathVariable Long backgroundId){
        return backgroundService.backgroundSelect(backgroundId);
    }

    // 배경 업로드
    @PostMapping("{projectId}/background/upload")
    public ResponseEntity<Response.Body> uploadBackground(@PathVariable Long projectId,@ModelAttribute RequestBackground.BackgroundUploadDto backgroundUploadDto) throws IOException {
        return backgroundService.backgroundUpload(projectId,backgroundUploadDto);
    }
}
