package hu.unideb.cartshare.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for authentication after logged in. It contains: refresh and access token.
 */
@Getter
@Setter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
