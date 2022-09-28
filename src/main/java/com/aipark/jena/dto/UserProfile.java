package com.aipark.jena.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile {
    private final String oauthId;
    private final String username;
    private final String email;
    private final String profileImg;
}
