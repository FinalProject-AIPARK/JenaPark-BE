package com.aipark.jena.config;

public interface ProjectDefault {
    String TITLE_DEFAULT = "프로젝트 명";

    String SEX_DEFAULT = "female";    //[female, male]
    String LANGUAGE_DEFAULT = "kor";  //[kor, chi, eng]

    Double SPEED_DEFAULT = 0.0;   //default : 0, step : 0.1, range : -0.5 ~ 0.5
    Double PITCH_DEFAULT = 0.0;   //default : 0, step : 0.1, range : -0.5 ~ 0.5
    Long VOLUME_DEFAULT = 50L;    //default : 50, step : 10, range : 0 ~ 100
    Double DURATION_SILENCE_DEFAULT = 0.5;    //default : 0.5, step : 0.1, range : 0.1 ~

    Boolean AUDIO_UPLOAD_DEFAULT = false;
    Boolean AUDIO_MERGE_DEFAULT = false;

    String BACKGROUND_DEFAULT = "https://jenapark.s3.ap-northeast-2.amazonaws.com/background/default/BG_GREEN.png";
}
