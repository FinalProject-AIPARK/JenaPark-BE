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
            throws IOException, ServletException {
        // 구글/카카오의 회원 정보를 DTO 로 변환
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        // 최초 로그인이라면 회원가입 처리한다.
        log.info("토큰 발행 시작");
        Response.TokenRes tokenRes = jwtTokenProvider.generateToken(authentication);
        // 토큰 확인
        log.info("accessToken = " + tokenRes.getAccessToken());
        log.info("refreshToken = " + tokenRes.getRefreshToken());

//        Cookie accessCookie =new Cookie("accessToken",tokenRes.getAccessToken());
//        Cookie refreshCookie =new Cookie("refreshToken",tokenRes.getAccessToken());

//        writeTokenResponse(response, tokenRes);
//        response.setContentType("text/html;charset=UTF-8");

//        response.addCookie(accessCookie);
//        response.addCookie(refreshCookie);
//        log.info("name: "+accessCookie.getName());
//        log.info("value: "+accessCookie.getValue());

        response.addHeader("Auth", tokenRes.getAccessToken());
        response.addHeader("Refresh", tokenRes.getRefreshToken());

//        response.setContentType("application/json;charset=UTF-8");

        getRedirectStrategy().sendRedirect(request,response,"/");
    }

    private void writeTokenResponse(HttpServletResponse response, Response.TokenRes token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}