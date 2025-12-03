package hu.unideb.cartshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.unideb.cartshare.component.JwtFilter;
import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.dto.request.GoogleLoginRequestDto;
import hu.unideb.cartshare.model.dto.request.RefreshTokenRequestDto;
import hu.unideb.cartshare.model.dto.request.TraditionalLoginRequestDto;
import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.LoginResponseDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.service.auth.AuthService;
import hu.unideb.cartshare.service.user.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "jwt.access.secret=testSecretKeyWhichShouldBeLongEnough123456",
        "jwt.refresh.secret=testSecretKeyWhichShouldBeLongEnough123456",
        "jwt.access.expiration=3600000",
        "jwt.refresh.expiration=86400000",
        "google.client.id=teszt-google-client-id"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private JwtFilter jwtFilter;
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ShouldReturnTokens_WhenCredentialsAreValid() throws Exception {
        TraditionalLoginRequestDto loginDto = new TraditionalLoginRequestDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("Password123");

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        when(authService.login(any(TraditionalLoginRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"));
    }

    @Test
    void register_ShouldReturnUser_WhenDataIsValid() throws Exception {
        UserRequestDto registerDto = new UserRequestDto();
        registerDto.setUsername("newuser");
        registerDto.setEmail("newuser@example.com");
        registerDto.setPassword("StrongPassword123");

        UserResponseDto responseDto = UserResponseDto.builder()
                .username("newuser")
                .email("newuser@example.com")
                .build();

        when(userService.createLocalUser(any(UserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void refresh_ShouldReturnNewTokens_WhenRefreshTokenIsValid() throws Exception {
        RefreshTokenRequestDto refreshDto = new RefreshTokenRequestDto();
        refreshDto.setRefreshToken("validRefreshToken");

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken("newAccessToken")
                .refreshToken("validRefreshToken")
                .build();

        when(authService.refresh(any(RefreshTokenRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"));
    }


    @Test
    void refresh_ShouldReturnUnauthorized_WhenRefreshTokenIsInvalid() throws Exception {
        RefreshTokenRequestDto refreshDto = new RefreshTokenRequestDto();
        refreshDto.setRefreshToken("invalidRefreshToken");

        when(authService.refresh(any(RefreshTokenRequestDto.class))).thenReturn(null);

        mockMvc.perform(post("/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenUsernameIsMissing() throws Exception {
        TraditionalLoginRequestDto loginDto = new TraditionalLoginRequestDto();
        loginDto.setPassword("Password123");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void oauthGoogleLogin_ShouldReturnTokens_WhenTokenIsValid() throws Exception {
        GoogleLoginRequestDto googleDto = new GoogleLoginRequestDto();
        googleDto.setToken("validGoogleToken");

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        when(authService.oauthGoogleLogin(any(GoogleLoginRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/oauth/google")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(googleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"));
    }

    @Test
    void oauthGoogleLogin_ShouldThrowException_WhenAuthFails() throws Exception {
        GoogleLoginRequestDto googleDto = new GoogleLoginRequestDto();
        googleDto.setToken("invalidGoogleToken");

        when(authService.oauthGoogleLogin(any(GoogleLoginRequestDto.class)))
                .thenThrow(new GeneralSecurityException("Google token invalid."));

        Exception exception = assertThrows(ServletException.class, () -> mockMvc.perform(post("/auth/oauth/google")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(googleDto))));

        assertInstanceOf(GeneralSecurityException.class, exception.getCause());
    }
}
