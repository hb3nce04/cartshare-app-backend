package hu.unideb.cartshare.service.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import hu.unideb.cartshare.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;

/**
 * Handles Google OAuth2 authentication logic. (Token verification)
 */
@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    @Value("${google.client.id}")
    private String CLIENT_ID;

    /**
     * Verifies the Google-based / signed token.
     * @param token token from Google
     * @return {@link GoogleIdToken.Payload} payload
     * @throws GeneralSecurityException N/A
     * @throws IOException N/A
     */
    public GoogleIdToken.Payload verifyToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new BusinessLogicException("Invalid Google token.");
        }
    }
}
