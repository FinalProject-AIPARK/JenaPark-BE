package com.aipark.jena.dto;

import com.aipark.jena.domain.Member;
import com.aipark.jena.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.aipark.jena.dto.ResponseAudio.AudioInfoDto;
import static com.aipark.jena.dto.ResponseVideo.HistoryVideo;

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
        private String audioModel;
        private String audioModelUrl;
        private Boolean audioUpload;
        private Boolean audioMerge;
        private String audioFileOriginName;
        private String audioFileUrl;
        private String downloadAudioUrl;
        private String avatarUrl;
        private Boolean checkText;
        private Boolean checkAudio;
        private Boolean checkAvatar;
        private List<AudioInfoDto> audioInfos;

        public static InitialProject of(Project project) {
            List<AudioInfoDto> audioInfoDtos = project.getAudioInfos()
                    .stream()
                    .map(AudioInfoDto::of)
                    .sorted(Comparator.comparing(AudioInfoDto::getAudioId))
                    .collect(Collectors.toList());

            return InitialProject.builder()
                    .projectId(project.getId())
                    .title(project.getTitle())
                    .audioModel(project.getAudioModel())
                    .audioModelUrl(project.getAudioModelUrl())
                    .sex(project.getSex())
                    .lang(project.getLang())
                    .speed(project.getSpeed())
                    .pitch(project.getPitch())
                    .volume(project.getVolume())
                    .durationSilence(project.getDurationSilence())
                    .backgroundUrl(project.getBackgroundUrl())
                    .audioModel(project.getAudioModel())
                    .audioModelUrl(project.getAudioModelUrl())
                    .audioUpload(project.getAudioUpload())
                    .audioMerge(project.getAudioMerge())
                    .audioFileOriginName(project.getAudioFileOriginName())
                    .audioFileUrl(project.getAudioFileUrl())
                    .downloadAudioUrl(project.getDownloadAudioUrl())
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
    public static class HistoryAll {
        private List<HistoryProject> historyProjects;
        private List<HistoryVideo> historyVideos;

        public static HistoryAll of(Member member) {
            return HistoryAll.builder()
                    .historyProjects(member.getProjects()
                            .stream()
                            .map(HistoryProject::of)
                            .sorted(Comparator.comparing(HistoryProject::getModifiedDate).reversed())
                            .collect(Collectors.toList()))
                    .historyVideos(member.getVideos()
                            .stream()
                            .map(HistoryVideo::of)
                            .sorted(Comparator.comparing(HistoryVideo::getCreateDate).reversed())
                            .collect(Collectors.toList()))
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
        private LocalDateTime createDate;
        private LocalDateTime modifiedDate;

        public static HistoryProject of(Project project) {
            return HistoryProject.builder()
                    .projectId(project.getId())
                    .title(project.getTitle())
                    .createDate(project.getCreatedDate())
                    .modifiedDate(project.getModifiedDate())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateTTSProject {
        private Long audioId;
        private String allText;
        private String audioFileUrl;

        public static UpdateTTSProject of(Long audioId, String allText, String audioFileUrl) {
            return UpdateTTSProject.builder()
                    .audioId(audioId)
                    .allText(allText)
                    .audioFileUrl(audioFileUrl)
                    .build();
        }
    }
}
