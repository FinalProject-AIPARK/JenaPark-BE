package com.aipark.jena.dto;

import com.aipark.jena.domain.AudioInfo;
import com.aipark.jena.domain.AudioSample;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AudioInfoDto {
        private Long audioID;
        private int lineNumber;
        private String splitText;
        private String audioFileUrl;
        private Double durationSilence;
        private Double pitch;
        private Double speed;
        private Long volume;

        public static ResponseAudio.AudioInfoDto of(AudioInfo audioInfo) {
            return AudioInfoDto.builder()
                    .audioID(audioInfo.getId())
                    .lineNumber(audioInfo.getLineNumber())
                    .splitText(audioInfo.getSplitText())
                    .audioFileUrl(audioInfo.getAudioFileUrl())
                    .durationSilence(audioInfo.getDurationSilence())
                    .pitch(audioInfo.getPitch())
                    .speed(audioInfo.getSpeed())
                    .volume(audioInfo.getVolume())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioStage1 {
        private List<AudioInfoDto> audioInfoDtos;
        private String text;
    }
}
