package hu.unideb.cartshare.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.unideb.cartshare.model.dto.request.RefreshTokenRequestDto;
import hu.unideb.cartshare.model.dto.request.TraditionalLoginRequestDto;
import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.LoginResponseDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.service.AuthService;
import hu.unideb.cartshare.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated TraditionalLoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Validated RefreshTokenRequestDto dto) {
        Optional<LoginResponseDto> response = Optional.ofNullable(authService.refresh(dto));
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // TODO: /oauth/google -> req: GoogleLoginRequestDto, res: LoginResponseDto
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Validated UserRequestDto dto) {
        // TODO: /register -> req: UserRequestDto, res: LoginResponseDto
        return ResponseEntity.ok(userService.create(dto));
    }
}
