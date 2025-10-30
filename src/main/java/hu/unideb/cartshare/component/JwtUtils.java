package hu.unideb.cartshare.component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.unideb.cartshare.model.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {
    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiration}")
    private int accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private int refreshExpiration;

    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID username) {
        return Jwts.builder().subject(String.valueOf(username)).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(accessSecretKey).compact();
    }

    public String generateRefreshToken(UUID username) {
        return Jwts.builder().subject(String.valueOf(username)).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshSecretKey).compact();
    }

    public String extractSubject(
            String token,
            boolean isRefreshToken) {
        return Jwts.parser().verifyWith(isRefreshToken ? refreshSecretKey : accessSecretKey).build().parseSignedClaims(token).getPayload()
                .getSubject();
    }

    public boolean validateToken(
            String token,
            UserDetailsImpl userDetails,
            boolean isRefreshToken) {
        final UUID id = UUID.fromString(extractSubject(token, isRefreshToken));
        return (id.equals(userDetails.getId()) && !isTokenExpired(token, isRefreshToken));
    }

    private boolean isTokenExpired(
            String token,
            boolean isRefreshToken) {
        return Jwts.parser().verifyWith(isRefreshToken ? refreshSecretKey : accessSecretKey).build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }
}
