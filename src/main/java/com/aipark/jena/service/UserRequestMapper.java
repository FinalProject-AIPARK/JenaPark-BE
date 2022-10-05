package com.aipark.jena.service;

import com.aipark.jena.dto.UserProfile;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Component
public class UserRequestMapper {
    public UserProfile toUserProfile(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserProfile.builder()
                .email((String) attributes.get("email"))
                .username((String) attributes.get("name"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }
}
