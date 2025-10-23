package hu.unideb.cartshare.mapper;

import org.mapstruct.Mapper;

import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;

@Mapper(componentModel = "spring")
public interface ListItemMapper {
    ListItemResponseDto toDto(ListItem entity);
}
