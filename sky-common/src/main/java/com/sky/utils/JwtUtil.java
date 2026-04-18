package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成 JWT
     * @param secretKeyBase64 Base64 编码的密钥（至少 32 字节）
     * @param ttlMillis       过期时间（毫秒）
     * @param claims          自定义信息
     * @return JWT 字符串
     */
    public static String createJWT(String secretKeyBase64, long ttlMillis, Map<String, Object> claims) {
        // 将 Base64 密钥转换为 SecretKey 对象
        SecretKey key = Keys.hmacShaKeyFor(secretKeyBase64.getBytes(StandardCharsets.UTF_8));

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .claims(claims)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析 JWT
     * @param secretKeyBase64 Base64 编码的密钥
     * @param token           JWT 字符串
     * @return Claims
     */
    public static Claims parseJWT(String secretKeyBase64, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKeyBase64.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}