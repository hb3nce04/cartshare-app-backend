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
    @NotEmpty(message = "You must give name")
    private String name;

    @NotNull(message = "You must give quantity")
    @Positive(message = "Invalid quantity (>0!)")
    private Double quantity = 1.0;

    @NotEmpty(message = "You must give unit")
    private String unit;

    @NotNull(message = "You must give state")
    private Boolean isChecked = false;
}
