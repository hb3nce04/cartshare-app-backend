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
    @NotEmpty(message = "You must give name")
    private String name;

    @NotNull(message = "You must give quantity")
    @Positive
    private Double quantity = 1.0;

    @NotEmpty(message = "You must give unit")
    private String unit;

    @NotEmpty(message = "You must give list id")
    private String listId;
}
