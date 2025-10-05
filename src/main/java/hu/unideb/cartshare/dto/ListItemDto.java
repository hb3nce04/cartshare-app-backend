package hu.unideb.cartshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ListItemDto {
    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private String id;

    @NotEmpty(message = "Kitöltése kötelező")
    private String name;

    @NotNull(message = "Kitöltése kötelező")
    private Boolean isChecked;
}
