package com.aipark.jena.dto;

import com.aipark.jena.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ResponseMember {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberInfo {
        private String username;
        private String email;
        private String profileImg;
        private LocalDate createDate;

        public static ResponseMember.MemberInfo of(Member member) {
            return MemberInfo.builder()
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .profileImg(member.getProfileImg())
                    .createDate(member.getCreatedDate())
                    .build();
        }
    }
}
