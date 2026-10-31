package org.example.dailyjournal;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    // 密钥 (实际项目中建议放在配置文件里，这里为了演示直接写死)
    // 注意：密钥长度必须足够长，否则 HS256 会报错
    private static final String SECRET_KEY = "my-daily-journal-secret-key-must-be-long-enough-for-hs256-algorithm!";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // 令牌有效期：7天
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    // 生成 Token
    public static String generateToken(Long userId, String phone) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("phone", phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析 Token，获取用户ID
    public static Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
