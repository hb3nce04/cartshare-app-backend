package hu.unideb.cartshare.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.component.JwtUtils;
import hu.unideb.cartshare.model.dto.request.RefreshTokenRequestDto;
import hu.unideb.cartshare.model.dto.request.TraditionalLoginRequestDto;
import hu.unideb.cartshare.model.dto.response.LoginResponseDto;
import hu.unideb.cartshare.model.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public LoginResponseDto login(TraditionalLoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
        final String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public LoginResponseDto refresh(RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();

        String username = jwtUtils.extractUsername(refreshToken, true);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtils.validateToken(refreshToken, userDetails, true)) {
            String newAccessToken = jwtUtils.generateAccessToken(username);
            return LoginResponseDto.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
        } else {
            return null;
        }
    }
}
