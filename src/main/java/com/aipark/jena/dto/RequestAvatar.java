package com.aipark.jena.dto;

import io.swagger.annotations.ApiModelProperty;
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
        @ApiModelProperty(value = "아바타 번호", example = "1", required = true)
        private Long avatarId;
        @ApiModelProperty(value = "악세사리 번호", example = "2", required = true)
        private Long accessoryId;
        @ApiModelProperty(value = "모자 번호", example = "3", required = true)
        private Long hatId;
        @ApiModelProperty(value = "의상 번호", example = "1", required = true)
        private Long clothesId;
        @ApiModelProperty(value = "프로젝트 번호", example = "1", required = true)
        private Long projectId;
    }
}
