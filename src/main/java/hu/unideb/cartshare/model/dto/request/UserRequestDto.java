package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for user registration.
 */
@Getter
@Setter
public class UserRequestDto {
    @NotEmpty(message = "Felhasználónév megadása kötelező.")
    private String username;

    @NotEmpty(message = "E-mail cím megadása kötelező.")
    @Pattern(regexp = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,64}", message = "Helytelen e-mail cím formátum.")
    private String email;

    @NotEmpty(message = "Jelszó megadása kötelező.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Helytelen jelszó formátum.")
    private String password;
}
