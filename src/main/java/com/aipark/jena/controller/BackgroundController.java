package com.aipark.jena.controller;

import com.aipark.jena.dto.RequestBackground;
import com.aipark.jena.dto.Response;
import com.aipark.jena.dto.ResponseBackground;
import com.aipark.jena.service.BackgroundService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.aipark.jena.dto.RequestBackground.*;
import static com.aipark.jena.dto.Response.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/")
public class BackgroundController {

    private final BackgroundService backgroundService;

    @GetMapping("/background")
    @ApiResponse(responseCode = "200", description = "배경리스트 조회 성공", content = @Content(schema = @Schema(implementation = ResponseBackground.BackgroundAll.class)))
    @ApiOperation("배경 리스트")
    public ResponseEntity<Body> backgroundList(){
        return backgroundService.backgroundList();
    }

    @PostMapping("{projectId}/background/{backgroundId}")
    @ApiOperation("배경 선택")
    public ResponseEntity<Body> backgroundSelect(@ApiParam(value = "프로젝트아이디") @PathVariable Long projectId, @ApiParam(value = "배경 아이디") @PathVariable Long backgroundId){
        return backgroundService.backgroundSelect(projectId,backgroundId);
    }

    @PostMapping("{projectId}/background/upload")
    @ApiOperation("베경 업로드")
    public ResponseEntity<Body> uploadBackground(@ApiParam(value = "프로젝트아이디") @PathVariable Long projectId, @ModelAttribute BackgroundUploadDto backgroundUploadDto) throws IOException {
        return backgroundService.backgroundUpload(projectId,backgroundUploadDto);
    }
}
