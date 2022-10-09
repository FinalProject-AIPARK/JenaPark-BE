package com.aipark.jena.dto;

import lombok.Getter;
import lombok.Setter;

public class RequestVideo {
    @Getter
    @Setter
    public static class ChangeTitle {
        private String title;
    }
}
