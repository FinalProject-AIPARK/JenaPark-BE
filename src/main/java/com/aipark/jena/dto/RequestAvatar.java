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
public class RequestAvatar {
    private String avatarName;
    private String thumbNail;
    private List accUrl;
    private List clothesUrl;
    private List attitudeUrl;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestCreateAvatar{

        private Long avatarId;
        private Long accessoryId;
        private Long attitudeId;
        private Long clothesId;
        private Long projectId;
    }
}
