package hu.unideb.cartshare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // TODO: /login -> req: TraditionalLoginRequestDto, res: LoginResponseDto
    // TODO: /oauth/google -> req: GoogleLoginRequestDto, res: LoginResponseDto
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Validated UserRequestDto dto) {
        // TODO: /register -> req: UserRequestDto, res: LoginResponseDto
        return ResponseEntity.ok(userService.create(dto));
    }
}
