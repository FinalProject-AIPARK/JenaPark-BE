package com.aipark.jena.dto;

import com.aipark.jena.domain.Member;
import com.aipark.jena.enums.Authority;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Pattern;

public class RequestMember {

    @Getter
    @Setter
    public static class SignUp {
        @ApiModelProperty(example = "test@test.com", required = true)
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @ApiModelProperty(example = "1234", required = true)
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;
        @ApiModelProperty(example = "1234", required = true)
        private String confirmPassword;
        @ApiModelProperty(example = "jena", required = true)
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
        @ApiModelProperty(example = "test@test.com", required = true)
        private String email;
        @ApiModelProperty(example = "1234", required = true)
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
