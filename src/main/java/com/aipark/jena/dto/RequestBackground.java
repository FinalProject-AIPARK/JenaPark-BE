package com.aipark.jena.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class RequestBackground {

    @Getter
    @Setter
    public static class BackgroundUploadDto {
        private Long projectId;
        private MultipartFile backgroundFile;
    }
}
