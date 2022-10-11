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
        @ApiModelProperty(example = "test123", required = true)
        @Pattern(regexp = "^([a-z0-9]*)$", message = "영문이랑 숫자로 5글자 이상~20글자이하(특수문자 x,@ x,)")
        private String email;
        @ApiModelProperty(example = "1q2w3e4r~~", required = true)
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 혼합하여 사용하세요.")
        private String password;
        @ApiModelProperty(example = "1q2w3e4r~~", required = true)
        private String confirmPassword;
        @ApiModelProperty(example = "jena", required = true)
        @Pattern(regexp = "(?=.*[0-9a-zA-Zㄱ-ㅎ가-힣]).{2,16}", message = "유저네임은 2~16자의 영문 대 소문자, 숫자와 한글만 사용 가능합니다.")
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
