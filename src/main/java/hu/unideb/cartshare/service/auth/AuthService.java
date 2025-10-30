package hu.unideb.cartshare.service.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.dto.request.GoogleLoginRequestDto;
import hu.unideb.cartshare.model.dto.request.RefreshTokenRequestDto;
import hu.unideb.cartshare.model.dto.request.TraditionalLoginRequestDto;
import hu.unideb.cartshare.model.dto.response.LoginResponseDto;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.service.UserDetailsServiceImpl;
import hu.unideb.cartshare.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public LoginResponseDto login(TraditionalLoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final String accessToken = jwtUtils.generateAccessToken(userDetails.getId());
        final String refreshToken = jwtUtils.generateRefreshToken(userDetails.getId());

        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public LoginResponseDto oauthGoogleLogin(GoogleLoginRequestDto dto) throws GeneralSecurityException, IOException {
        String token = dto.getToken();

        GoogleIdToken.Payload payload = googleAuthService.verifyToken(token);

        String email = payload.getEmail();
        String username = email.substring(0, email.indexOf("@"));
        String sub = payload.getSubject();

        User user = userService.findOrCreateGoogleUser(email, username, sub);

        final String accessToken = jwtUtils.generateAccessToken(user.getId());
        final String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public LoginResponseDto refresh(RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();

        UUID id = UUID.fromString(jwtUtils.extractSubject(refreshToken, true));
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserById(id);

        if (jwtUtils.validateToken(refreshToken, userDetails, true)) {
            String newAccessToken = jwtUtils.generateAccessToken(id);
            return LoginResponseDto.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
        } else {
            return null;
        }
    }
}
