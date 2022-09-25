package com.aipark.jena.config;

public class ProjectDefault {
    public static String TITLE_DEFAULT = "프로젝트 명";

    public static String SEX_DEFAULT = "female";    //[female, male]
    public static String LANGUAGE_DEFAULT = "kor";  //[kor, chi, eng]

    public static String SPEED_DEFAULT = "0.0";   //default : 0, step : 0.1, range : -0.5 ~ 0.5
    public static String PITCH_DEFAULT = "0.0";   //default : 0, step : 0.1, range : -0.5 ~ 0.5
    public static Long VOLUME_DEFAULT = 50L;    //default : 50, step : 10, range : 0 ~ 100
    public static String DURATION_SILENCE_DEFAULT = "0.5";    //default : 0.5, step : 0.1, range : 0.1 ~

    public static Boolean AUDIO_UPLOAD_DEFAULT = false;

    public static String BACKGROUND_DEFAULT = "https://jenapark.s3.ap-northeast-2.amazonaws.com/background/default/BG_GREEN.png";
}
