package com.aipark.jena.service;

import com.aipark.jena.config.jwt.JwtTokenProvider;
import com.aipark.jena.config.security.SecurityUtil;
import com.aipark.jena.domain.Member;
import com.aipark.jena.domain.MemberRepository;
import com.aipark.jena.dto.RequestMember;
import com.aipark.jena.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.aipark.jena.dto.Response.Body;
import static com.aipark.jena.dto.Response.TokenRes;
import static com.aipark.jena.dto.ResponseMember.MemberInfo;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public ResponseEntity<Body> signUp(RequestMember.SignUp signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            return response.fail("재확인 비밀번호를 다시 확인해주십시오.", HttpStatus.UNAUTHORIZED);
        }
        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        Member member = signUpDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return response.success("회원가입에 성공했습니다.");
    }

    @Transactional
    public ResponseEntity<Body> login(RequestMember.Login loginDto) {
        if (memberRepository.findByEmail(loginDto.getEmail()).orElse(null) == null) {
            return response.fail("해당유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // LoginDto email, password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 실제 검증 (사용자 비밀번호 체크)
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenRes tokenRes = jwtTokenProvider.generateToken(authentication);

        String oldToken= redisTemplate.opsForValue().get("AT:" + authentication.getName());
        // Redis 에서 해당 Member email 로 저장된 Access Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (oldToken != null) {
            // Access Token 삭제
            redisTemplate.delete("AT:" + authentication.getName());

            // AccessToken 만료
            Long expiration = jwtTokenProvider.getExpiration(oldToken);
            redisTemplate.opsForValue()
                    .set(oldToken, "anotherAccess", expiration, TimeUnit.MILLISECONDS);
        }

        // AccessToken Redis 저장 (expirationTime 으로 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("AT:" + authentication.getName(),
                        tokenRes.getAccessToken(),
                        tokenRes.getAccessTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);


        // RefreshToken Redis 저장 (expirationTime 으로 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),
                        tokenRes.getRefreshToken(),
                        tokenRes.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return response.success(tokenRes, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<Body> reissue(RequestMember.Reissue reissue) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // Access Token 에서 Member email 가져옴.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // Redis 에서 Member email 을 기반으로 저장된 Refresh Token 을 가져옴.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        // 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 새로운 토큰 생성
        TokenRes tokenInfo = jwtTokenProvider.generateToken(authentication);

        // RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Body> logout(RequestMember.Logout logoutDto) {
        // Access Token 검증
        if (!jwtTokenProvider.validateToken(logoutDto.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // Access Token 에서 Member email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logoutDto.getAccessToken());


        // Redis 에서 해당 Member email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("AT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("AT:" + authentication.getName());
        }
        // Redis 에서 해당 Member email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logoutDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(logoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<Body> getInfo() {
        Optional<Member> memberRes = memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail());
        if (memberRes.isPresent()) {
            return response.success(MemberInfo.of(memberRes.get()),"유저 정보를 불러왔습니다.",HttpStatus.OK);
        } else {
            return response.fail("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Body> validateHandling(Errors errors){
        Map<String,String> validatorResult = new HashMap<>();

        // 유효성 검사에 실패한 필드 목록을 받음
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s",error.getField());
            validatorResult.put(validKeyName,error.getDefaultMessage());
        }
        return response.fail(validatorResult,"유효성 검증 실패",HttpStatus.BAD_REQUEST);
    }
}
