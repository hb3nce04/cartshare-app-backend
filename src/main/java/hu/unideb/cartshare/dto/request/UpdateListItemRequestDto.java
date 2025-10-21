package hu.unideb.cartshare.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateListItemRequestDto {
    @NotEmpty(message = "Kitöltése kötelező")
    private String name;

    @NotNull(message = "Kitöltése kötelező")
    @Positive
    private Integer quantity = 1;

    @NotEmpty(message = "Kitöltése kötelező")
    private String unit;

    @NotNull(message = "Kitöltése kötelező")
    private Boolean isChecked = false;
}
