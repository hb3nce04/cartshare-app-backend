package hu.unideb.cartshare.model.dto.response;

import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponseDto {
    private UUID id;
    private String name;
    private Set<ListItemResponseDto> items;
}
