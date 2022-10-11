package com.aipark.jena.oauth;

import com.aipark.jena.config.jwt.JwtTokenProvider;
import com.aipark.jena.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        log.info("토큰 발행 시작");
        Response.TokenRes tokenRes = jwtTokenProvider.generateToken(authentication);
        log.info("accessToken = " + tokenRes.getAccessToken());
        log.info("refreshToken = " + tokenRes.getRefreshToken());

        Cookie accessCookie =new Cookie("accessToken",tokenRes.getAccessToken());
        Cookie refreshCookie =new Cookie("refreshToken",tokenRes.getAccessToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        log.info("name: "+accessCookie.getName());
        log.info("value: "+accessCookie.getValue());

        response.sendRedirect("https://jennapark.netlify.app/");
        // getRedirectStrategy().sendRedirect(request,response,"/");
    }
}