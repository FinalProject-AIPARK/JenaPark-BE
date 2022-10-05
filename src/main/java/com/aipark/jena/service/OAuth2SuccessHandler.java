package com.aipark.jena.service;

import com.aipark.jena.config.jwt.JwtTokenProvider;
import com.aipark.jena.dto.Token;
import com.aipark.jena.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        UserProfile userProfile = toUserProfile(oAuth2User);

        // 최초 로그인이라면 회원가입 처리한다.

        // Access Token, Refresh Token 생성 및 발급한다.
        // Token token = tokenService.generateToken(userDto.getEmail(), "USER");
        // Response.TokenRes tokenRes = jwtTokenProvider.generateToken(UserProfile.getEmail(), "USER");

        // 토큰 포함하여 리다이렉트한다.
        String targetUrl;
        targetUrl = UriComponentsBuilder.fromUriString("/home")
                .queryParam("token", "token")
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public UserProfile toUserProfile(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserProfile.builder()
                .email((String) attributes.get("email"))
                .username((String) attributes.get("name"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }
}