package hu.unideb.cartshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListDto {
    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private String id;

    @NotEmpty(message = "Kitöltése kötelező")
    private String name;

    private List<ListItemDto> items;

}
