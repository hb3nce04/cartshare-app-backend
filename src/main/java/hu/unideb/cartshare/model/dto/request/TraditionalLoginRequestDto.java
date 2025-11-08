package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for authenticating traditionally.
 */
@Getter
@Setter
public class TraditionalLoginRequestDto {
    @NotEmpty(message = "Felhasználónév megadása kötelező.")
    private String username;

    @NotEmpty(message = "Jelszó megadása kötelező.")
    private String password;
}
