package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraditionalLoginRequestDto {
    @NotEmpty(message = "Kitöltése kötelező")
    private String username;

    @NotEmpty(message = "Kitöltése kötelező")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Helytelen formátum")
    private String password;
}
