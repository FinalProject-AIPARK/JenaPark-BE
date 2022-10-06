package com.aipark.jena.oauth;

import com.aipark.jena.config.jwt.JwtTokenProvider;
import com.aipark.jena.dto.Response;
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
        // 구글/카카오의 회원 정보를 DTO 로 변환
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        // MemberProfile memberProfile = toMemberProfile(oAuth2User);

        // 최초 로그인이라면 회원가입 처리한다.

        // Access Token, Refresh Token 생성 및 발급한다.
        // Token token = tokenService.generateToken(memberProfile.getEmail(), "USER");
        Response.TokenRes tokenRes = jwtTokenProvider.generateToken(authentication);

        // 토큰 포함하여 리다이렉트한다.
        String targetUrl;
        targetUrl = UriComponentsBuilder.fromUriString("/home")
                .queryParam("token", tokenRes)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public MemberProfile toMemberProfile(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return MemberProfile.builder()
                .email((String) attributes.get("email"))
                .username((String) attributes.get("name"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }
}