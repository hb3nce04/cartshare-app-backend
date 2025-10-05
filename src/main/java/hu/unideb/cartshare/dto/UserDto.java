package hu.unideb.cartshare.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotEmpty(message = "Kitöltése kötelező")
    private String username;

    @NotEmpty(message = "Kitöltése kötelező")
    private String email;

    @NotEmpty(message = "Kitöltése kötelező")
    private String password;
}
