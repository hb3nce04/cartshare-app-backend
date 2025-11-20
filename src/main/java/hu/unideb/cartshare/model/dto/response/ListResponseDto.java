package hu.unideb.cartshare.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for getting lists.
 */
@Getter
@Setter
public class ListResponseDto {
    private UUID id;
    private String name;
    private Set<ListItemResponseDto> items;
}
