package com.aipark.jena.dto;

import com.aipark.jena.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseProject {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InitialProject {
        private Long projectId;
        private String title;
        private String sex;
        private String lang;
        private Double speed;
        private Double pitch;
        private Long volume;
        private Double durationSilence;
        private String backgroundUrl;
        private Boolean audioUpload;
        private Boolean audioMerge;
        private String audioFileUrl;

        public static InitialProject of(Project project) {
            return InitialProject.builder()
                    .projectId(project.getId())
                    .title(project.getTitle())
                    .sex(project.getSex())
                    .lang(project.getLang())
                    .speed(project.getSpeed())
                    .pitch(project.getPitch())
                    .volume(project.getVolume())
                    .durationSilence(project.getDurationSilence())
                    .backgroundUrl(project.getBackgroundUrl())
                    .audioUpload(project.getAudioUpload())
                    .audioMerge(project.getAudioMerge())
                    .audioFileUrl(project.getAudioFileUrl())
                    .build();
        }
    }
}
