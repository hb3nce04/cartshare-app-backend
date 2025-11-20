package hu.unideb.cartshare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Response DTO for getting list items.
 */
@Getter
@Setter
public class ListItemResponseDto {
    private UUID id;
    private Double quantity;
    private String unit;
    private String name;
    private Boolean isChecked;
}
