package hu.unideb.cartshare.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    // /login -> req: TraditionalLoginRequestDto, res: LoginResponseDto
    // /oauth/google -> req: GoogleLoginRequestDto, res: LoginResponseDto
    // /register -> req: UserRequestDto, res: LoginResponseDto
}
