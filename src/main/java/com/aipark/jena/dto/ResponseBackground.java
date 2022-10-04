package com.aipark.jena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseBackground {

    private Long bgId;
    private String bgName;
    private String bgUrl;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BackgroundAll{
        private List<BackgroundDefault> backgroundDefaults;
        private List<BackgroundUpload> backgroundUploads;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BackgroundDefault{
        private Long bgId;
        private String bgName;
        private String bgUrl;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BackgroundUpload {
        private Long bgId;
        private String bgName;
        private String bgUrl;
    }
}
