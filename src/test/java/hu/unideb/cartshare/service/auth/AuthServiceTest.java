package hu.unideb.cartshare.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.dto.request.GoogleLoginRequestDto;
import hu.unideb.cartshare.model.dto.request.RefreshTokenRequestDto;
import hu.unideb.cartshare.model.dto.request.TraditionalLoginRequestDto;
import hu.unideb.cartshare.model.dto.response.LoginResponseDto;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.service.user.UserDetailsServiceImpl;
import hu.unideb.cartshare.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GoogleAuthService googleAuthService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private UUID userId;
    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        userDetails = UserDetailsImpl.builder()
                .id(userId)
                .username("tester")
                .password("encoded")
                .build();
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsValid() {
        TraditionalLoginRequestDto dto = new TraditionalLoginRequestDto();
        dto.setUsername("tester");
        dto.setPassword("secret");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(userId)).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken(userId)).thenReturn("refresh-token");

        LoginResponseDto result = authService.login(dto);

        assertNotNull(result);
        assertEquals("access-token", result.getAccessToken());
        assertEquals("refresh-token", result.getRefreshToken());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtils).generateAccessToken(userId);
        verify(jwtUtils).generateRefreshToken(userId);
    }

    @Test
    void oauthGoogleLogin_shouldReturnTokens_whenGoogleTokenValid() throws GeneralSecurityException, IOException {
        GoogleLoginRequestDto dto = new GoogleLoginRequestDto();
        dto.setToken("google-token");

        GoogleIdToken.Payload payload = mock(GoogleIdToken.Payload.class);
        when(googleAuthService.verifyToken("google-token")).thenReturn(payload);
        when(payload.getEmail()).thenReturn("tester@gmail.com");
        when(payload.getSubject()).thenReturn("sub123");

        when(userService.findOrCreateGoogleUser("tester@gmail.com", "tester", "sub123"))
                .thenReturn(user);
        when(jwtUtils.generateAccessToken(userId)).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken(userId)).thenReturn("refresh-token");

        LoginResponseDto result = authService.oauthGoogleLogin(dto);

        assertNotNull(result);
        assertEquals("access-token", result.getAccessToken());
        assertEquals("refresh-token", result.getRefreshToken());

        verify(googleAuthService).verifyToken("google-token");
        verify(userService).findOrCreateGoogleUser("tester@gmail.com", "tester", "sub123");
        verify(jwtUtils).generateAccessToken(userId);
        verify(jwtUtils).generateRefreshToken(userId);
    }

    @Test
    void refresh_shouldReturnNewAccessToken_whenRefreshTokenValid() {
        RefreshTokenRequestDto dto = new RefreshTokenRequestDto();
        dto.setRefreshToken("valid-refresh");

        when(jwtUtils.extractSubject("valid-refresh", true)).thenReturn(userId.toString());
        when(userDetailsService.loadUserById(userId)).thenReturn(userDetails);
        when(jwtUtils.validateToken("valid-refresh", userDetails, true)).thenReturn(true);
        when(jwtUtils.generateAccessToken(userId)).thenReturn("new-access-token");

        LoginResponseDto result = authService.refresh(dto);

        assertNotNull(result);
        assertEquals("new-access-token", result.getAccessToken());
        assertEquals("valid-refresh", result.getRefreshToken());

        verify(jwtUtils).validateToken("valid-refresh", userDetails, true);
        verify(jwtUtils).generateAccessToken(userId);
    }

    @Test
    void refresh_shouldReturnNull_whenTokenInvalid() {
        RefreshTokenRequestDto dto = new RefreshTokenRequestDto();
        dto.setRefreshToken("invalid-refresh");

        when(jwtUtils.extractSubject("invalid-refresh", true)).thenReturn(userId.toString());
        when(userDetailsService.loadUserById(userId)).thenReturn(userDetails);
        when(jwtUtils.validateToken("invalid-refresh", userDetails, true)).thenReturn(false);

        LoginResponseDto result = authService.refresh(dto);

        assertNull(result);
        verify(jwtUtils, never()).generateAccessToken(any());
    }
}
