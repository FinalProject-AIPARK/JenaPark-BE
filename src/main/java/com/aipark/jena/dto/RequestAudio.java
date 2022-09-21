package com.aipark.jena.dto;

import lombok.Getter;
import lombok.Setter;

public class RequestAudio {
    @Getter
    @Setter
    public static class AudioSampleDto {
        private String lang;
        private String sex;
    }
}
