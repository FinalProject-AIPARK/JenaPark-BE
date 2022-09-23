package com.aipark.jena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseAvatar {
    private String avatarName;
    private String thumbNail;
    private List accUrl;
    private List clothesUrl;
    private List attitudeUrl;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ResponseAvatarList{

        private Long id;
        private String name;
        private String thumbNail;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ResponseCreateAvatar{

        private Long avatarId;
        private Long accessoryId;
        private Long attitudeId;
        private Long clothesId;

    }
}
