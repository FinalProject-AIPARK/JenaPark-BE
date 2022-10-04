package com.aipark.jena.dto;

import com.aipark.jena.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.aipark.jena.dto.ResponseAudio.*;

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
        private String audioFileOriginName;
        private String audioFileUrl;
        private String avatarUrl;
        private Boolean checkText;
        private Boolean checkAudio;
        private Boolean checkAvatar;
        private List<AudioInfoDto> audioInfos;

        public static InitialProject of(Project project) {
            List<AudioInfoDto> audioInfoDtos = project.getAudioInfos()
                    .stream()
                    .map(AudioInfoDto::of)
                    .collect(Collectors.toList());

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
                    .audioFileOriginName(project.getAudioFileOriginName())
                    .audioFileUrl(project.getAudioFileUrl())
                    .avatarUrl(project.getAvatarUrl())
                    .checkText(project.getCheckText())
                    .checkAudio(project.getCheckAudio())
                    .checkAvatar(project.getCheckAvatar())
                    .audioInfos(audioInfoDtos)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HistoryProject {
        private Long projectId;
        private String title;
        private String thumbnail;
        private LocalDate createDate;
        private LocalDateTime modifiedDate;

        public static HistoryProject of(Project project) {
            return HistoryProject.builder()
                    .projectId(project.getId())
                    .title(project.getTitle())
                    .thumbnail(project.getAvatarUrl())
                    .createDate(project.getCreatedDate())
                    .modifiedDate(project.getModifiedDate())
                    .build();
        }
    }
}
