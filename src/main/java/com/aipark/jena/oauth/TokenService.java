//package com.aipark.jena.oauth;
//
//import com.aipark.jena.dto.Response;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.token.Token;
//
//import javax.annotation.PostConstruct;
//import java.util.Base64;
//import java.util.Date;
//
//public class TokenService { // JwtTokenProvider
//    private static final String AUTHORITIES_KEY = "auth";
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//
//    public Token generateToken(String uid, String role) {
//        long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
//        long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
//
//        Claims claims = Jwts.claims().setSubject(uid);
//        claims.put("role", role);
//
//        long now = (new Date()).getTime();
//
//        // AccessToken 생성
//        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//
//        String accessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(AUTHORITIES_KEY, authorities)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        // RefreshToken 생성
//        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
//
//        String refreshToken = Jwts.builder()
//                .setExpiration(refreshTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return Response.TokenRes.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .accessTokenExpirationTime(accessTokenExpiresIn.getTime())
//                .refreshTokenExpirationTime(refreshTokenExpiresIn.getTime())
//                .build();
//    }
//
//
////    public boolean verifyToken(String token) {
////        try {
////            Jws<Claims> claims = Jwts.parser()
////                    .setSigningKey(secretKey)
////                    .parseClaimsJws(token);
////            return claims.getBody()
////                    .getExpiration()
////                    .after(new Date());
////        } catch (Exception e) {
////            return false;
////        }
////    }
////
////
////    public String getUid(String token) {
////        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
////    }
//}
