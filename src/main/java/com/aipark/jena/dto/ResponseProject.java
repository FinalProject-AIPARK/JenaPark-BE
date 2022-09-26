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

        public static InitialProject of(Project project) {
            return InitialProject.builder()
                    .projectId(project.getId())
                    .title(project.getTitle())
                    .sex(project.getSex())
                    .lang(project.getLang())
                    .speed(Double.parseDouble(project.getSpeed()))
                    .pitch(Double.parseDouble(project.getPitch()))
                    .volume(project.getVolume())
                    .durationSilence(Double.parseDouble(project.getDurationSilence()))
                    .backgroundUrl(project.getBackgroundUrl())
                    .audioUpload(project.getAudioUpload())
                    .build();
        }
    }
}
