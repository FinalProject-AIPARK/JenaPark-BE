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
import org.springframework.web.util.UriComponentsBuilder;

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

//        Cookie accessCookie =new Cookie("accessToken",tokenRes.getAccessToken());
//        Cookie refreshCookie =new Cookie("refreshToken",tokenRes.getAccessToken());
//
//        accessCookie.setPath("/");
//        refreshCookie.setPath("/");
//
//        accessCookie.setSecure(false);
//        refreshCookie.setSecure(false);
//
//        response.addCookie(accessCookie);
//        response.addCookie(refreshCookie);

        String targetUri;
        targetUri = UriComponentsBuilder.fromUriString("https://jennapark.netlify.app/")
                .queryParam("accessToken" , tokenRes.getAccessToken())
                .queryParam("refreshToken",tokenRes.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request,response,targetUri);
    }
//    private void writeTokenResponse(HttpServletResponse response, Response.TokenRes token)
//            throws IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        response.addHeader("Acess", token.getAccessToken());
//        response.addHeader("Refresh", token.getRefreshToken());
//        response.setContentType("application/json;charset=UTF-8");
//
//        var writer = response.getWriter();
//        writer.println(objectMapper.writeValueAsString(token));
//        writer.flush();
//        response.sendRedirect("https://jennapark.netlify.app/");
//    }
}