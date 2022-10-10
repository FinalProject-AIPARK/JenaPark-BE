package com.aipark.jena.dto;

import com.aipark.jena.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ResponseVideo {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HistoryVideo {
        private Long videoId;
        private String title;
        private String avatarUrl;
        private String backgroundUrl;
        private String videoFileUrl;
        private String downloadFileUrl;
        private LocalDateTime createDate;

        public static HistoryVideo of(Video video) {
            return HistoryVideo.builder()
                    .videoId(video.getId())
                    .title(video.getTitle())
                    .avatarUrl(video.getAvatarUrl())
                    .backgroundUrl(video.getBackgroundUrl())
                    .videoFileUrl(video.getVideoFileUrl())
                    .downloadFileUrl(video.getDownloadFileUrl())
                    .createDate(video.getCreatedDate())
                    .build();
        }
    }
}
