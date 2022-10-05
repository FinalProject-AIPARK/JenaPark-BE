package com.aipark.jena.oauth;

import com.aipark.jena.domain.Member;
import com.aipark.jena.enums.Authority;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor
@Getter
@Builder
public class MemberProfile {
    private final String username;
    private final String email;
    private final String profileImg;
}
