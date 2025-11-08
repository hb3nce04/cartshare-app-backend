package hu.unideb.cartshare.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating a specific list item.
 */
@Getter
@Setter
public class UpdateListItemRequestDto {
    @NotEmpty(message = "Elemnév megadása kötelező.")
    private String name;

    @NotNull(message = "Mennyiség megadása kötelező.")
    @Positive(message = "Helytelen mennyiség. (>0!)")
    private Double quantity = 1.0;

    @NotEmpty(message = "Mértékegység megadása kötelező.")
    private String unit;

    @NotNull(message = "Állapot megadása kötelező.")
    private Boolean isChecked = false;
}
