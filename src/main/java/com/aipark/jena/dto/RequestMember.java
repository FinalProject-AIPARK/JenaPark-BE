package com.aipark.jena.dto;

import com.aipark.jena.domain.Member;
import com.aipark.jena.enums.Authority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RequestMember {

    @Getter
    @Setter
    public static class SignUp {
        private String email;
        private String password;
        private String username;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .username(username)
                    .authority(Authority.ROLE_USER)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class Login {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Setter
    public static class Reissue {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        private String accessToken;
        private String refreshToken;
    }
}
