package hu.unideb.cartshare.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListRequestDto {
    @NotEmpty(message = "Kitöltése kötelező")
    private String name;
}
