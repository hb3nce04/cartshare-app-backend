package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a list.
 */
@Getter
@Setter
public class ListRequestDto {
    @NotEmpty(message = "Listanév megadása kötelező.")
    private String name;
}
