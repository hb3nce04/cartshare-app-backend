package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for getting an access token with an existing refresh token.
 */
@Getter
@Setter
public class RefreshTokenRequestDto {
    @NotEmpty(message = "Token megadása kötelező.")
    private String refreshToken;
}
