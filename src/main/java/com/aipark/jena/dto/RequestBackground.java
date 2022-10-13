package com.aipark.jena.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class RequestBackground {

    @Getter
    public static class BackgroundUploadDto {
        private MultipartFile backgroundFile;
    }
}
