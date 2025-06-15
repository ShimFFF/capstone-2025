package naeilmolae.global.config.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.refreshExpiration}")
    private long jwtAccessExpiration;

    @Value("${jwt.accessExpiration}")
    private long jwtRefreshExpiration;


    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String memberId, String role, TokenType tokenType) {
        Date now = new Date();
        Date expiration;
        // 분기 나눠야 ㅐ-해, 리프레쉬 토큰과 액세스 토큰의 만료시간이 다르니까
        if (TokenType.ACCESS.equals(tokenType)) { // 액세스 토큰
            expiration=calculateExpirationDate(now, jwtAccessExpiration);
        } else { // 리프레쉬 토큰
            expiration=calculateExpirationDate(now, jwtRefreshExpiration);
        }

        Claims claims = Jwts.claims().setSubject(memberId); // JWT payload 에 저장되는 정보단위
        claims.put("role",  role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // 토큰 생성
    public TokenInfo generateToken(String memberId, String subject) {
        String accessToken = generateToken(memberId, subject, TokenType.ACCESS);
        String refreshToken = generateToken(memberId, subject, TokenType.REFRESH);

        return new TokenInfo(accessToken, refreshToken);
    }

    // 토큰 만료일 계산
    private Date calculateExpirationDate(Date createdDate, long jwtExpiration) {
        return new Date(createdDate.getTime() + jwtExpiration);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 유효하지 않은 토큰 처리
        }
    }

    // 토큰에서 정보 추출
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 토큰에서 회원 아이디 추출
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    // 토큰에서 발급 시간 추출 후 LocalDateTime으로 변환
    public LocalDateTime getIssuedAt(String token) {
        Date issuedAt = getClaims(token).getIssuedAt();
        if (issuedAt == null) {
            return null; // 발급 시간이 없을 경우 null 반환
        }
        return issuedAt.toInstant()
                .atZone(ZoneId.systemDefault()) // 시스템 시간대에 맞춰 변환
                .toLocalDateTime();
    }

}
