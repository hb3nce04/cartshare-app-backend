package hu.unideb.cartshare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
