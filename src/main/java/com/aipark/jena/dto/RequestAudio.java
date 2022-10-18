package com.aipark.jena.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class RequestAudio {
    @Getter
    @Setter
    public static class AudioSampleDto {
        @ApiModelProperty(example = "kor", required = true)
        private String lang;
        @ApiModelProperty(example = "male", required = true)
        private String sex;
    }

    @Getter
    @Setter
    public static class AudioUploadDto {
        private MultipartFile audioFile;
    }

    @Getter
    @Setter
    public static class AudioSampleSearchDto{
        private String lang;
        private String sex;
        private String keyword;
    }
}
