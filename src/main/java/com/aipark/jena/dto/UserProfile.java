package com.aipark.jena.dto;

import com.aipark.jena.domain.Member;
import com.aipark.jena.enums.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile {
    private final String username;
    private final String email;
    private final String profileImg;

    public Member toMember() {
        return new Member(username, email, profileImg, Authority.ROLE_USER);
    }
}
