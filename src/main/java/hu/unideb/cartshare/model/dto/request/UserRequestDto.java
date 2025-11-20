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
    @NotEmpty(message = "You must give username")
    private String username;

    @NotEmpty(message = "You must give e-mail address")
    @Pattern(regexp = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,64}", message = "Invalid e-mail address format")
    private String email;

    @NotEmpty(message = "You must give password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Invalid password format")
    private String password;
}
