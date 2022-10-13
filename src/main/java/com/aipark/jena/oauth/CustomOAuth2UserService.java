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
        // loadUser
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // registrationId : 구글인지 카카오인지 구별, userNameAttributeName : PK (구글 기본 제공, 네이버/카카오는 지원X)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2Attribute : OAuth2User 의 DTO
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        saveOrUpdate(oAuth2Attribute);

        var memberAttribute = oAuth2Attribute.convertToMap();
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }

    private Member saveOrUpdate(OAuth2Attribute attribute){
        Member member = memberRepository.findByEmailAndOauthId(attribute.getEmail(), attribute.getOauthId())
                .orElse(attribute.toEntity());

//        if (member.getOauthId() == "g") {
//            member.setOauthId("k");
//        } else if (member.getOauthId() == "k") {
//            member.setOauthId("g");
//        }

        return memberRepository.save(member);
    }
}
