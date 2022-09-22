package com.aipark.jena.dto;

import lombok.Getter;
import lombok.Setter;

public class RequestProject {

    @Getter
    @Setter
    public static class CreateTTS {
        private Long projectID;
        private String avatarName;
        private String sex;
        private String lang;
        private Long durationSilence;
        private Long pitch;
        private Long speed;
        private Long volume;
        private String text;
    }

    @Getter
    @Setter
    public static class ModifyTTS {
        private Long projectID;
        private Long audioID;
        private Long durationSilence;
        private Long pitch;
        private Long speed;
        private Long volume;
        private String text;
    }

    @Getter
    @Setter
    public static class CreateSTF {
        private Long projectID;
        private String avatarName;
        private Long audioID;
        private String bgName;
    }

    @Getter
    @Setter
    public static class VideoReq {
        private Long jobID;
    }
}
