package com.aipark.jena.oauth;

import com.aipark.jena.domain.Member;
import com.aipark.jena.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 로그인 성공정보(code, access token)를 바탕으로 DefaultOAuth2UserService 객체 생성한다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // 2. 생성된 Service 객체로 부터 User를 받는다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 3. 받은 User로 부터 user 정보를 받는다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 4. SuccessHandler가 사용할 수 있도록 등록해준다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(oAuth2Attribute);

        var memberAttribute = oAuth2Attribute.convertToMap();
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }

    private Member saveOrUpdate(OAuth2Attribute attribute){
        Member member = memberRepository.findByEmail(attribute.getEmail())
                .orElse(attribute.toEntity());
        return memberRepository.save(member);
    }
}
