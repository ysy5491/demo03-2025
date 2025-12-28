package com.ll.demo03.domain.auth.auth.service;

import com.ll.demo03.domain.member.member.entity.Member;
import com.ll.demo03.global.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    // 토큰 생성
    public String genToken(Member member, long expireSeconds) { // expireSeconds: 토큰 만료 시간 (초 단위)
        // 토큰에 담길 데이터
        Claims claims = Jwts
                .claims()
                .add("id", member.getId())
                .add("username", member.getUsername())
                .build();
        // 토큰 발급 시간 및 만료 시간 설정
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000 * expireSeconds);
        // 토큰 생성 (반환 값: JWT 문자열)
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    // 토큰에서 데이터 꺼내기
    public Map<String, Object> getDataFrom(String token) {
        Claims payload = Jwts.parser()
                .setSigningKey(AppConfig.getJwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return Map.of(
                "id", payload.get("id", Integer.class),
                "username", payload.get("username", String.class)
        );
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리프레시 토큰 생성 (랜덤 문자열)
    public String genRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}