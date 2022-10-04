package com.aipark.jena.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class RequestAudio {
    @Getter
    @Setter
    public static class AudioSampleDto {
        private String lang;
        private String sex;
    }

    @Getter
    @Setter
    public static class AudioUploadDto {
        private MultipartFile audioFile;
    }
}
