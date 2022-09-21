package com.aipark.jena.dto;

import com.aipark.jena.domain.AudioSample;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseAudio {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AudioSampleDto {
        private String name;
        private String sex;
        private String audioFileUrl;
        private String lang;

        public static ResponseAudio.AudioSampleDto of(AudioSample audioSample) {
            return AudioSampleDto.builder()
                    .name(audioSample.getName())
                    .sex(audioSample.getSex())
                    .lang(audioSample.getLang())
                    .audioFileUrl(audioSample.getAudioFileUrl())
                    .build();
        }
    }
}
