package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for authenticating with Google OAuth.
 */
@Getter
@Setter
public class GoogleLoginRequestDto {
    @NotEmpty(message = "Google token megadása kötelező.")
    private String token;
}
