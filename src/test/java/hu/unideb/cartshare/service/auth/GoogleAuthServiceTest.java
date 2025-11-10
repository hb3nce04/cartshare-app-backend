package hu.unideb.cartshare.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import hu.unideb.cartshare.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleAuthServiceTest {

    private GoogleAuthService googleAuthService;

    @BeforeEach
    void setUp() {
        googleAuthService = new GoogleAuthService();
        ReflectionTestUtils.setField(googleAuthService, "CLIENT_ID", "test-client-id");
    }

    @Test
    void verifyToken_shouldReturnPayload_whenTokenIsValid() throws Exception {
        GoogleIdTokenVerifier verifierMock = mock(GoogleIdTokenVerifier.class);
        GoogleIdToken tokenMock = mock(GoogleIdToken.class);
        GoogleIdToken.Payload payloadMock = mock(GoogleIdToken.Payload.class);

        when(tokenMock.getPayload()).thenReturn(payloadMock);
        when(verifierMock.verify("valid-token")).thenReturn(tokenMock);

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> mocked =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class,
                             (builder, context) -> {
                                 when(builder.setAudience(Collections.singletonList("test-client-id"))).thenReturn(builder);
                                 when(builder.build()).thenReturn(verifierMock);
                             })) {

            var result = googleAuthService.verifyToken("valid-token");

            assertNotNull(result);
            assertEquals(payloadMock, result);
            verify(verifierMock).verify("valid-token");
        }
    }

    @Test
    void verifyToken_shouldThrowBusinessLogicException_whenTokenInvalid() throws Exception {
        GoogleIdTokenVerifier verifierMock = mock(GoogleIdTokenVerifier.class);
        when(verifierMock.verify("invalid-token")).thenReturn(null);

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> mocked =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class,
                             (builder, context) -> {
                                 when(builder.setAudience(Collections.singletonList("test-client-id"))).thenReturn(builder);
                                 when(builder.build()).thenReturn(verifierMock);
                             })) {

            BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                    () -> googleAuthService.verifyToken("invalid-token"));

            assertEquals("Hibás Google ID token.", ex.getMessage());
        }
    }

    @Test
    void verifyToken_shouldThrowIOException_whenVerifierFails() throws Exception {
        GoogleIdTokenVerifier verifierMock = mock(GoogleIdTokenVerifier.class);
        when(verifierMock.verify(anyString())).thenThrow(new IOException("network error"));

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> mocked =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class,
                             (builder, context) -> {
                                 when(builder.setAudience(Collections.singletonList("test-client-id"))).thenReturn(builder);
                                 when(builder.build()).thenReturn(verifierMock);
                             })) {

            assertThrows(IOException.class, () -> googleAuthService.verifyToken("any-token"));
        }
    }

    @Test
    void verifyToken_shouldThrowGeneralSecurityException_whenSecurityFails() throws Exception {
        GoogleIdTokenVerifier verifierMock = mock(GoogleIdTokenVerifier.class);
        when(verifierMock.verify(anyString())).thenThrow(new GeneralSecurityException("security error"));

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> mocked =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class,
                             (builder, context) -> {
                                 when(builder.setAudience(Collections.singletonList("test-client-id"))).thenReturn(builder);
                                 when(builder.build()).thenReturn(verifierMock);
                             })) {

            assertThrows(GeneralSecurityException.class, () -> googleAuthService.verifyToken("any-token"));
        }
    }
}
