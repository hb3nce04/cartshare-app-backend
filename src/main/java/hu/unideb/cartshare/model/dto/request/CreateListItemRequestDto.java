package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a list.
 */
@Getter
@Setter
public class CreateListItemRequestDto {
    @NotEmpty(message = "Elemnév megadása kötelező.")
    private String name;

    @NotNull(message = "Mennyiség megadása kötelező.")
    @Positive
    private Double quantity = 1.0;

    @NotEmpty(message = "Mértékegység megadása kötelező.")
    private String unit;

    @NotEmpty(message = "Lista megadása kötelező.")
    private String listId;
}
