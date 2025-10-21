package hu.unideb.cartshare.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.entity.ListItem;

@Mapper(componentModel = "spring")
public interface ListItemMapper {
    Set<ListItemResponseDto> toDto(Set<ListItem> listItems);
}
