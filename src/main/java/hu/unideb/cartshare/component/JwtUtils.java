package hu.unideb.cartshare.component;

import hu.unideb.cartshare.model.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JWT utility class for handling access and refresh tokens.
 * This class is not designed for extension.
 */
@Component
public final class JwtUtils {
    /**
     * Secret key for access token generation.
     */
    @Value("${jwt.access.secret}")
    private String accessSecret;

    /**
     * Secret key for refresh token generation.
     */
    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    /**
     * Expiration time in milliseconds for access tokens.
     */
    @Value("${jwt.access.expiration}")
    private int accessExpiration;

    /**
     * Expiration time in milliseconds for refresh tokens.
     */
    @Value("${jwt.refresh.expiration}")
    private int refreshExpiration;

    /**
     * HMAC SHA key for access token signing.
     */
    private SecretKey accessSecretKey;

    /**
     * HMAC SHA key for refresh token signing.
     */
    private SecretKey refreshSecretKey;

    /**
     * Initializes the secret keys after dependency injection.
     * Creates HMAC SHA keys from the configured secrets.
     */
    @PostConstruct
    public void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(
                accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(
                refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a new JWT access token for the given user ID.
     *
     * @param username the user ID to include in the token subject
     * @return the generated JWT access token
     */
    public String generateAccessToken(final UUID username) {
        return Jwts.builder().subject(String.valueOf(username))
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + accessExpiration))
                .signWith(accessSecretKey)
                .compact();
    }

    /**
     * Generates a new JWT refresh token for the given user ID.
     *
     * @param username the user ID to include in the token subject
     * @return the generated JWT refresh token
     */
    public String generateRefreshToken(final UUID username) {
        return Jwts.builder().subject(String.valueOf(username))
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshSecretKey)
                .compact();
    }

    /**
     * Extracts the subject (user ID) from a JWT token.
     *
     * @param token the JWT token to parse
     * @param isRefreshToken whether the token is a refresh token
     * @return the subject (user ID) from the token
     */
    public String extractSubject(
            final String token,
            final boolean isRefreshToken) {
        return Jwts.parser()
                .verifyWith(isRefreshToken ? refreshSecretKey : accessSecretKey)
                .build().parseSignedClaims(token).getPayload()
                .getSubject();
    }

    /**
     * Validates a JWT token against the provided user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @param isRefreshToken whether the token is a refresh token
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(
            final String token,
            final UserDetailsImpl userDetails,
            final boolean isRefreshToken) {
        final UUID id = UUID.fromString(extractSubject(token, isRefreshToken));
        return (id.equals(userDetails.getId())
                && !isTokenExpired(token, isRefreshToken));
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token the JWT token to check
     * @param isRefreshToken whether the token is a refresh token
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(
            final String token,
            final boolean isRefreshToken) {
        return Jwts.parser()
                .verifyWith(isRefreshToken ? refreshSecretKey : accessSecretKey)
                .build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }
}
