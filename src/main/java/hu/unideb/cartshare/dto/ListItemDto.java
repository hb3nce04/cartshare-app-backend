package hu.unideb.cartshare.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ListItemDto {
    private String id;

    private String name;

    private Boolean isChecked;
}
