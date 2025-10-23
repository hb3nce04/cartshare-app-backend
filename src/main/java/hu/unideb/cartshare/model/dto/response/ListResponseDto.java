package hu.unideb.cartshare.model.dto.response;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponseDto {
    private String id;
    private String name;
    private Set<ListItemResponseDto> items;
}
