package hu.unideb.cartshare.model.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

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
