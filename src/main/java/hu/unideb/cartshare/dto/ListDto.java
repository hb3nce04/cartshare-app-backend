package hu.unideb.cartshare.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListDto {
    private String id;

    private String name;

    private List<ListItemDto> items;

}
