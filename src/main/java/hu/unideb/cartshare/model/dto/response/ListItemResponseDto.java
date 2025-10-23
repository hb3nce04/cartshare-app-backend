package hu.unideb.cartshare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListItemResponseDto {
    private String id;
    // DOUBLE
    private Integer quantity;
    private String unit;
    private String name;
    private Boolean isChecked;
}
