package hu.unideb.cartshare.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListItemResponseDto {
    private String id;
    private String name;
    private Boolean isChecked;
}
